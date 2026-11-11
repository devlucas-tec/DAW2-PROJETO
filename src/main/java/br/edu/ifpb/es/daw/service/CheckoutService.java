package br.edu.ifpb.es.daw.service;

import br.edu.ifpb.es.daw.exception.EntidadeNaoEncontradaException;
import br.edu.ifpb.es.daw.exception.EstadoInvalidoException;
import br.edu.ifpb.es.daw.model.*;
import br.edu.ifpb.es.daw.model.enums.EtapaPedido;
import br.edu.ifpb.es.daw.model.enums.MetodoPagamento;
import br.edu.ifpb.es.daw.model.enums.StatusPagamento;
import br.edu.ifpb.es.daw.model.enums.StatusPedido;
import br.edu.ifpb.es.daw.repository.*;
import br.edu.ifpb.es.daw.rest.dto.request.CheckoutRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Transforma o carrinho do cliente em uma {@link Compra} com um pedido por vendedor.
 * Tudo acontece em uma única transação: qualquer erro (estoque, cupom, endereço) desfaz a operação inteira.
 */
@Service
public class CheckoutService {

    private static final int PAGE_SIZE = 10;
    private static final String ALFABETO_CODIGO = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    @Autowired private CarrinhoRepository carrinhoRepository;
    @Autowired private ClienteRepository clienteRepository;
    @Autowired private CupomRepository cupomRepository;
    @Autowired private ProdutoRepository produtoRepository;
    @Autowired private CompraRepository compraRepository;
    @Autowired private PedidoRepository pedidoRepository;
    @Autowired private PagamentoRepository pagamentoRepository;
    @Autowired private EnderecoService enderecoService;
    @Autowired private PedidoService pedidoService;
    @Autowired private PedidoHistoricoService historicoService;
    @Autowired private UsuarioAutenticadoService usuarioAutenticado;

    @Transactional
    public Compra finalizar(CheckoutRequestDTO dto) {
        Cliente cliente = clienteLogado();
        Carrinho carrinho = carrinhoRepository.findByClienteId(cliente.getId())
                .filter(c -> !c.getItens().isEmpty())
                .orElseThrow(() -> new EstadoInvalidoException("O carrinho está vazio."));

        Endereco endereco = enderecoService.buscarPorId(dto.getIdEndereco());
        Cupom cupom = buscarCupom(dto.getCodigoCupom());

        // Trava os produtos do carrinho para que duas compras simultâneas não vendam o mesmo estoque.
        List<Long> idsProdutos = carrinho.getItens().stream().map(item -> item.getProduto().getId()).toList();
        Map<Long, Produto> produtos = produtoRepository.buscarParaAtualizacao(idsProdutos).stream()
                .collect(Collectors.toMap(Produto::getId, Function.identity()));

        validarItens(carrinho, produtos);

        // Um pedido por vendedor, mantendo a ordem em que os itens foram adicionados.
        Map<Long, List<ItemCarrinho>> itensPorVendedor = new LinkedHashMap<>();
        for (ItemCarrinho item : carrinho.getItens()) {
            Produto produto = produtos.get(item.getProduto().getId());
            itensPorVendedor.computeIfAbsent(produto.getVendedor().getId(), id -> new ArrayList<>()).add(item);
        }

        BigDecimal subtotalCompra = carrinho.getItens().stream()
                .map(item -> produtos.get(item.getProduto().getId()).getPreco()
                        .multiply(BigDecimal.valueOf(item.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal descontoCompra = cupom != null ? cupom.getValorDesconto().min(subtotalCompra) : BigDecimal.ZERO;

        Compra compra = new Compra();
        compra.setCodigo(gerarCodigo());
        compra.setCliente(cliente);
        compra.setCupom(cupom);
        compra.setMetodoPagamento(dto.getMetodoPagamento());
        compra.setValorSubtotal(subtotalCompra);
        compra.setValorDesconto(descontoCompra);
        compra = compraRepository.save(compra);

        BigDecimal descontoDistribuido = BigDecimal.ZERO;
        int restantes = itensPorVendedor.size();
        BigDecimal totalCompra = BigDecimal.ZERO;

        for (List<ItemCarrinho> itensDoVendedor : itensPorVendedor.values()) {
            restantes--;
            BigDecimal subtotalPedido = itensDoVendedor.stream()
                    .map(item -> produtos.get(item.getProduto().getId()).getPreco()
                            .multiply(BigDecimal.valueOf(item.getQuantidade())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            // O desconto do cupom é rateado proporcionalmente; o último pedido fica com o resto do arredondamento.
            BigDecimal descontoPedido = restantes == 0
                    ? descontoCompra.subtract(descontoDistribuido)
                    : descontoCompra.multiply(subtotalPedido).divide(subtotalCompra, 2, RoundingMode.HALF_UP);
            descontoDistribuido = descontoDistribuido.add(descontoPedido);

            Pedido pedido = criarPedido(compra, cliente, cupom, endereco, descontoPedido);
            for (ItemCarrinho itemCarrinho : itensDoVendedor) {
                Produto produto = produtos.get(itemCarrinho.getProduto().getId());
                ItemPedido item = new ItemPedido();
                item.setId(new ItemPedidoId(pedido.getId(), produto.getId()));
                item.setPedido(pedido);
                item.setProduto(produto);
                item.setQuantidade(itemCarrinho.getQuantidade());
                item.setPrecoUnitario(produto.getPreco());
                pedido.getItens().add(item);
                produto.setEstoque(produto.getEstoque() - itemCarrinho.getQuantidade());
            }
            pedido.recalcularValorTotal();
            historicoService.registrar(pedido, EtapaPedido.PEDIDO_CONFIRMADO,
                    "Pedido realizado na compra " + compra.getCodigo() + ".");
            criarPagamento(pedido, dto.getMetodoPagamento());
            pedido = pedidoRepository.save(pedido);

            compra.getPedidos().add(pedido);
            totalCompra = totalCompra.add(pedido.getValorTotal());
        }

        compra.setValorTotal(totalCompra);
        carrinho.getItens().clear();
        carrinhoRepository.save(carrinho);
        return compraRepository.save(compra);
    }

    public Page<Compra> minhas(int page) {
        return compraRepository.findByClienteId(usuarioAutenticado.getUsuario().getId(),
                PageRequest.of(Math.max(page, 0), PAGE_SIZE,
                        Sort.by(Sort.Direction.DESC, "dataCompra").and(Sort.by(Sort.Direction.DESC, "id"))));
    }

    public Compra buscarPorCodigo(String codigo) {
        Compra compra = compraRepository.findByCodigo(codigo)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Compra", "codigo", codigo));
        usuarioAutenticado.validarProprioUsuario(compra.getCliente().getId());
        return compra;
    }

    private void validarItens(Carrinho carrinho, Map<Long, Produto> produtos) {
        for (ItemCarrinho item : carrinho.getItens()) {
            Produto produto = produtos.get(item.getProduto().getId());
            if (produto == null || !produto.isDisponivelParaVenda()) {
                throw new EstadoInvalidoException("O produto '" + item.getProduto().getNome()
                        + "' não está mais disponível. Remova-o do carrinho para continuar.");
            }
            if (item.getQuantidade() > produto.getEstoque()) {
                throw new EstadoInvalidoException("Estoque insuficiente para o produto '" + produto.getNome()
                        + "'. Disponível: " + produto.getEstoque() + ", no carrinho: " + item.getQuantidade() + ".");
            }
        }
    }

    private Pedido criarPedido(Compra compra, Cliente cliente, Cupom cupom, Endereco endereco, BigDecimal desconto) {
        Pedido pedido = new Pedido();
        pedido.setCompra(compra);
        pedido.setCliente(cliente);
        pedido.setCupom(cupom);
        pedido.setEnderecoEntrega(EnderecoEntrega.de(endereco));
        pedido.setValorDesconto(desconto);
        pedido.setStatus(StatusPedido.CONFIRMADO);
        return pedidoRepository.save(pedido);
    }

    private void criarPagamento(Pedido pedido, MetodoPagamento metodo) {
        Pagamento pagamento = new Pagamento();
        pagamento.setPedido(pedido);
        pagamento.setMetodo(metodo);
        pagamento.setValorPago(pedido.getValorTotal());
        // Cartão é simulado e aprovado na hora; PIX e boleto aguardam a confirmação do vendedor.
        pagamento.setStatus(metodo == MetodoPagamento.CARTAO ? StatusPagamento.APROVADO : StatusPagamento.PENDENTE);
        pedido.setPagamento(pagamentoRepository.save(pagamento));
        if (pagamento.getStatus() == StatusPagamento.APROVADO) {
            historicoService.registrar(pedido, EtapaPedido.PAGAMENTO_APROVADO, "Pagamento com cartão aprovado.");
        }
    }

    private Cupom buscarCupom(String codigo) {
        if (codigo == null || codigo.isBlank()) {
            return null;
        }
        Cupom cupom = cupomRepository.findByCodigoIgnoreCase(codigo.trim())
                .orElseThrow(() -> new EstadoInvalidoException("O cupom '" + codigo + "' não existe."));
        pedidoService.validarCupom(cupom);
        return cupom;
    }

    private Cliente clienteLogado() {
        Long id = usuarioAutenticado.getUsuario().getId();
        return clienteRepository.findById(id)
                .orElseThrow(() -> new AccessDeniedException("Apenas clientes podem finalizar compras."));
    }

    private String gerarCodigo() {
        String codigo;
        do {
            StringBuilder sb = new StringBuilder("CP").append(LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd")));
            for (int i = 0; i < 6; i++) {
                sb.append(ALFABETO_CODIGO.charAt(RANDOM.nextInt(ALFABETO_CODIGO.length())));
            }
            codigo = sb.toString();
        } while (compraRepository.existsByCodigo(codigo));
        return codigo;
    }
}
