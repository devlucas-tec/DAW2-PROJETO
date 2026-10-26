package br.edu.ifpb.es.daw.service;

import br.edu.ifpb.es.daw.exception.EntidadeNaoEncontradaException;
import br.edu.ifpb.es.daw.exception.EstadoInvalidoException;
import br.edu.ifpb.es.daw.model.Cliente;
import br.edu.ifpb.es.daw.model.Cupom;
import br.edu.ifpb.es.daw.model.ItemPedido;
import br.edu.ifpb.es.daw.model.Pagamento;
import br.edu.ifpb.es.daw.model.Pedido;
import br.edu.ifpb.es.daw.model.Produto;
import br.edu.ifpb.es.daw.model.Usuario;
import br.edu.ifpb.es.daw.model.enums.StatusPagamento;
import br.edu.ifpb.es.daw.model.enums.StatusPedido;
import br.edu.ifpb.es.daw.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PedidoService {

    private static final int PAGE_SIZE = 10;

    @Autowired
    private PedidoRepository repository;

    @Autowired
    private UsuarioAutenticadoService usuarioAutenticado;

    @Transactional
    public Pedido criar(Cliente cliente, Cupom cupom) {
        validarCupom(cupom);
        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setCupom(cupom);
        pedido.setStatus(StatusPedido.CRIADO);
        pedido.recalcularValorTotal();
        return repository.save(pedido);
    }

    public Page<Pedido> recuperarTodos(int page) {
        Usuario usuario = usuarioAutenticado.getUsuario();
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by(Sort.Direction.DESC, "dataPedido"));
        return switch (usuario.getRole()) {
            case ADMIN -> repository.findAll(pageable);
            case CLIENTE -> repository.buscarPorCliente(usuario.getId(), pageable);
            case VENDEDOR -> repository.buscarPorVendedor(usuario.getId(), pageable);
        };
    }

    public Pedido buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Pedido", id));
    }

    public Pedido buscarComAcesso(Long id) {
        Pedido pedido = buscarPorId(id);
        usuarioAutenticado.validarAcessoPedido(pedido);
        return pedido;
    }

    public Pedido buscarComoDono(Long id) {
        Pedido pedido = buscarPorId(id);
        usuarioAutenticado.validarDonoPedido(pedido);
        return pedido;
    }

    @Transactional
    public Pedido fechar(Long id) {
        Pedido pedido = buscarComoDono(id);
        validarStatus(pedido, StatusPedido.CRIADO, "confirmado");
        if (pedido.getItens().isEmpty()) {
            throw new EstadoInvalidoException("Não é possível confirmar um pedido sem itens.");
        }
        validarCupom(pedido.getCupom());
        for (ItemPedido item : pedido.getItens()) {
            Produto produto = item.getProduto();
            if (item.getQuantidade() > produto.getEstoque()) {
                throw new EstadoInvalidoException("Estoque insuficiente para o produto '" + produto.getNome()
                        + "'. Disponível: " + produto.getEstoque() + ", solicitado: " + item.getQuantidade());
            }
        }
        pedido.getItens().forEach(item ->
                item.getProduto().setEstoque(item.getProduto().getEstoque() - item.getQuantidade()));
        pedido.recalcularValorTotal();
        pedido.setStatus(StatusPedido.CONFIRMADO);
        return repository.save(pedido);
    }

    @Transactional
    public Pedido cancelar(Long id) {
        Pedido pedido = buscarComoDono(id);
        if (pedido.getStatus() != StatusPedido.CRIADO && pedido.getStatus() != StatusPedido.CONFIRMADO) {
            throw new EstadoInvalidoException(
                    "Só é possível cancelar pedidos CRIADOS ou CONFIRMADOS. Status atual: " + pedido.getStatus());
        }
        if (pedido.getEntrega() != null) {
            throw new EstadoInvalidoException(
                    "O pedido já foi enviado para entrega e não pode mais ser cancelado. Solicite uma devolução.");
        }
        if (pedido.getStatus() == StatusPedido.CONFIRMADO) {
            devolverEstoque(pedido);
        }
        Pagamento pagamento = pedido.getPagamento();
        if (pagamento != null) {
            if (pagamento.getStatus() == StatusPagamento.APROVADO) {
                pagamento.setStatus(StatusPagamento.ESTORNADO);
            } else if (pagamento.getStatus() == StatusPagamento.PENDENTE) {
                pagamento.setStatus(StatusPagamento.RECUSADO);
            }
        }
        pedido.setStatus(StatusPedido.CANCELADO);
        return repository.save(pedido);
    }

    @Transactional
    public void remover(Long id) {
        Pedido pedido = buscarPorId(id);
        if (pedido.getStatus() != StatusPedido.CRIADO && pedido.getStatus() != StatusPedido.CANCELADO) {
            throw new EstadoInvalidoException("Só é possível remover pedidos CRIADOS ou CANCELADOS.");
        }
        if (pedido.getPagamento() != null || pedido.getEntrega() != null || !pedido.getDevolucoes().isEmpty()) {
            throw new EstadoInvalidoException(
                    "O pedido possui pagamento, entrega ou devolução registrados e não pode ser removido.");
        }
        repository.delete(pedido);
        repository.flush();
    }

    public void devolverEstoque(Pedido pedido) {
        pedido.getItens().forEach(item ->
                item.getProduto().setEstoque(item.getProduto().getEstoque() + item.getQuantidade()));
    }

    public void validarStatus(Pedido pedido, StatusPedido esperado, String operacao) {
        if (pedido.getStatus() != esperado) {
            throw new EstadoInvalidoException("O pedido precisa estar " + esperado + " para ser " + operacao
                    + ". Status atual: " + pedido.getStatus());
        }
    }

    private void validarCupom(Cupom cupom) {
        if (cupom != null && !cupom.isValido()) {
            throw new EstadoInvalidoException("O cupom '" + cupom.getCodigo() + "' está inativo ou expirado.");
        }
    }
}
