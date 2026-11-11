package br.edu.ifpb.es.daw.seed;

import br.edu.ifpb.es.daw.config.StartupListener;
import br.edu.ifpb.es.daw.exception.EstadoInvalidoException;
import br.edu.ifpb.es.daw.model.*;
import br.edu.ifpb.es.daw.model.enums.*;
import br.edu.ifpb.es.daw.repository.*;
import br.edu.ifpb.es.daw.service.AvaliacaoService;
import br.edu.ifpb.es.daw.service.PedidoHistoricoService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SeedService {

    private static final int QUANTIDADE = 50;
    /** Os produtos são distribuídos entre os 10 primeiros vendedores (5 produtos cada). */
    private static final int VENDEDORES_COM_PRODUTOS = 10;

    private final AdminRepository adminRepository;
    private final ClienteRepository clienteRepository;
    private final VendedorRepository vendedorRepository;
    private final CategoriaRepository categoriaRepository;
    private final ProdutoRepository produtoRepository;
    private final EnderecoRepository enderecoRepository;
    private final CupomRepository cupomRepository;
    private final CarrinhoRepository carrinhoRepository;
    private final ItemCarrinhoRepository itemCarrinhoRepository;
    private final CompraRepository compraRepository;
    private final PedidoRepository pedidoRepository;
    private final PedidoHistoricoRepository pedidoHistoricoRepository;
    private final ItemPedidoRepository itemPedidoRepository;
    private final PagamentoRepository pagamentoRepository;
    private final EntregaRepository entregaRepository;
    private final DevolucaoRepository devolucaoRepository;
    private final AvaliacaoRepository avaliacaoRepository;
    private final AvaliacaoService avaliacaoService;
    private final PasswordEncoder passwordEncoder;
    private final StartupListener startupListener;

    public SeedService(
            AdminRepository adminRepository,
            ClienteRepository clienteRepository,
            VendedorRepository vendedorRepository,
            CategoriaRepository categoriaRepository,
            ProdutoRepository produtoRepository,
            EnderecoRepository enderecoRepository,
            CupomRepository cupomRepository,
            CarrinhoRepository carrinhoRepository,
            ItemCarrinhoRepository itemCarrinhoRepository,
            CompraRepository compraRepository,
            PedidoRepository pedidoRepository,
            PedidoHistoricoRepository pedidoHistoricoRepository,
            ItemPedidoRepository itemPedidoRepository,
            PagamentoRepository pagamentoRepository,
            EntregaRepository entregaRepository,
            DevolucaoRepository devolucaoRepository,
            AvaliacaoRepository avaliacaoRepository,
            AvaliacaoService avaliacaoService,
            PasswordEncoder passwordEncoder,
            StartupListener startupListener) {

        this.adminRepository = adminRepository;
        this.clienteRepository = clienteRepository;
        this.vendedorRepository = vendedorRepository;
        this.categoriaRepository = categoriaRepository;
        this.produtoRepository = produtoRepository;
        this.enderecoRepository = enderecoRepository;
        this.cupomRepository = cupomRepository;
        this.carrinhoRepository = carrinhoRepository;
        this.itemCarrinhoRepository = itemCarrinhoRepository;
        this.compraRepository = compraRepository;
        this.pedidoRepository = pedidoRepository;
        this.pedidoHistoricoRepository = pedidoHistoricoRepository;
        this.itemPedidoRepository = itemPedidoRepository;
        this.pagamentoRepository = pagamentoRepository;
        this.entregaRepository = entregaRepository;
        this.devolucaoRepository = devolucaoRepository;
        this.avaliacaoRepository = avaliacaoRepository;
        this.avaliacaoService = avaliacaoService;
        this.passwordEncoder = passwordEncoder;
        this.startupListener = startupListener;
    }

    @Transactional
    public void limpar() {
        avaliacaoRepository.deleteAllInBatch();
        devolucaoRepository.deleteAllInBatch();
        entregaRepository.deleteAllInBatch();
        pagamentoRepository.deleteAllInBatch();
        pedidoHistoricoRepository.deleteAllInBatch();
        itemPedidoRepository.deleteAllInBatch();
        pedidoRepository.deleteAllInBatch();
        compraRepository.deleteAllInBatch();
        itemCarrinhoRepository.deleteAllInBatch();
        carrinhoRepository.deleteAllInBatch();
        enderecoRepository.deleteAllInBatch();
        cupomRepository.deleteAllInBatch();
        produtoRepository.removerTodasAsImagens();
        produtoRepository.deleteAllInBatch();
        categoriaRepository.deleteAllInBatch();
        clienteRepository.deleteAll();
        vendedorRepository.deleteAll();
        adminRepository.deleteAll(adminRepository.findAll().stream()
                .filter(admin -> !StartupListener.ADMIN_EMAIL.equalsIgnoreCase(admin.getEmail()))
                .toList());
        startupListener.criarAdminPadraoSeNaoExistir();
    }

    @Transactional
    public void popular() {
        if (clienteRepository.count() > 0 || produtoRepository.count() > 0) {
            throw new EstadoInvalidoException("O banco já possui dados. Use DELETE /seed/limpar ou POST /seed/resetar.");
        }

        criarAdmins();
        List<Vendedor> vendedores = criarVendedores();
        List<Cliente> clientes = criarClientes();
        List<Categoria> categorias = criarCategorias();
        List<Produto> produtos = criarProdutos(categorias, vendedores);
        List<Endereco> enderecos = criarEnderecos(clientes);
        List<Cupom> cupons = criarCupons();
        criarCarrinhos(clientes, produtos);
        criarCompras(clientes, enderecos, produtos, cupons);
        produtos.forEach(avaliacaoService::atualizarResumoDoProduto);
    }

    private void criarAdmins() {
        for (int i = 1; i <= QUANTIDADE; i++) {
            Admin admin = new Admin();
            admin.setNome("Administrador " + i);
            admin.setEmail("admin" + i + "@sistema.com");
            admin.setSenha(passwordEncoder.encode("admin123"));
            admin.setRole(Role.ADMIN);
            adminRepository.save(admin);
        }
    }

    private List<Vendedor> criarVendedores() {
        String[] nomesVendedor = {
                "Tech Store", "Modas", "Eletro Shop", "Casa & Cia", "Pet Center",
                "Livros & Cia", "Sports Zone", "Gamer Zone", "Farmácia +", "Auto Peças"
        };
        String[] segmentos = {
                "Eletrônicos", "Moda", "Eletrodomésticos", "Casa", "Pets",
                "Livros", "Esportes", "Games", "Saúde", "Automotivo"
        };

        List<Vendedor> vendedores = new ArrayList<>();
        for (int i = 1; i <= QUANTIDADE; i++) {
            String segmento = segmentos[(i - 1) % segmentos.length];
            String nomeBase = nomesVendedor[(i - 1) % nomesVendedor.length];

            Vendedor vendedor = new Vendedor();
            vendedor.setNome(nomeBase + " " + i);
            vendedor.setEmail("vendedor" + i + "@" + semAcentos(segmento).toLowerCase().replace(" ", "") + ".com");
            vendedor.setSenha(passwordEncoder.encode("senha123"));
            vendedor.setRole(Role.VENDEDOR);
            vendedor.setRazaoSocial(nomeBase + " " + i + " Ltda");
            vendedor.setCnpjCpf(String.format("%02d.%03d.%03d/0001-%02d", i, i * 3, i * 7, i % 99 + 1));
            vendedores.add(vendedorRepository.save(vendedor));
        }
        return vendedores;
    }

    private List<Cliente> criarClientes() {
        String[] nomesCliente = {
                "Alex", "Maria", "João", "Ana", "Pedro",
                "Lucia", "Carlos", "Fernanda", "Rafael", "Juliana"
        };
        String[] sobrenomes = {
                "Pereira", "Silva", "Santos", "Oliveira", "Costa",
                "Souza", "Lima", "Alves", "Rocha", "Gomes"
        };

        List<Cliente> clientes = new ArrayList<>();
        for (int i = 1; i <= QUANTIDADE; i++) {
            Cliente cliente = new Cliente();
            cliente.setNome(nomesCliente[(i - 1) % nomesCliente.length] + " " + sobrenomes[(i - 1) % sobrenomes.length] + " " + i);
            cliente.setEmail("cliente" + i + "@email.com");
            cliente.setSenha(passwordEncoder.encode("senha123"));
            cliente.setRole(Role.CLIENTE);
            cliente.setTelefone("839999" + String.format("%05d", i));
            clientes.add(clienteRepository.save(cliente));
        }
        return clientes;
    }

    private List<Categoria> criarCategorias() {
        String[] nomesCategoria = {
                "Eletrônicos", "Roupas", "Calçados", "Informática", "Games",
                "Livros", "Esportes", "Casa", "Jardim", "Pets",
                "Beleza", "Saúde", "Automotivo", "Bebês", "Brinquedos",
                "Ferramentas", "Papelaria", "Música", "Filmes", "Alimentos"
        };

        List<Categoria> categorias = new ArrayList<>();
        for (int i = 1; i <= QUANTIDADE; i++) {
            String nomeBase = nomesCategoria[(i - 1) % nomesCategoria.length];
            Categoria categoria = new Categoria();
            categoria.setNome(nomeBase + " " + i);
            categoria.setDescricao("Categoria de " + nomeBase.toLowerCase() + " - linha " + i);
            categorias.add(categoriaRepository.save(categoria));
        }
        return categorias;
    }

    private List<Produto> criarProdutos(List<Categoria> categorias, List<Vendedor> vendedores) {
        String[] nomesProduto = {
                "Notebook Gamer", "Fone Bluetooth", "Camiseta Básica", "Tênis Running",
                "Mouse Sem Fio", "Teclado Mecânico", "Monitor 24\"", "SSD 1TB",
                "Cadeira Gamer", "Headset Pro", "Smartphone", "Tablet", "Smartwatch",
                "Câmera Digital", "Impressora", "Roteador Wi-Fi", "HD Externo",
                "Pendrive 64GB", "Webcam HD", "Caixa de Som"
        };
        // Palavra-chave usada para buscar uma foto real do tipo de produto (loremflickr.com).
        String[] temasImagem = {
                "laptop", "headphones", "tshirt", "sneakers",
                "computermouse", "keyboard", "monitor", "harddrive",
                "officechair", "headset", "smartphone", "tablet", "smartwatch",
                "camera", "printer", "router", "harddrive",
                "usb", "webcam", "speaker"
        };

        List<Produto> produtos = new ArrayList<>();
        for (int i = 1; i <= QUANTIDADE; i++) {
            int tipo = (i - 1) % nomesProduto.length;
            String tema = temasImagem[tipo];
            Produto produto = new Produto();
            produto.setNome(nomesProduto[tipo] + " " + i);
            produto.setDescricao("Descrição detalhada do produto: " + nomesProduto[tipo] + " - modelo " + i);
            produto.setPreco(new BigDecimal("99.90").add(BigDecimal.valueOf(i * 50L)));
            // Alguns produtos com estoque baixo, para o painel do vendedor ter alertas.
            produto.setEstoque(i % 7 == 0 ? 6 : 20 + i);
            produto.setImagemUrl(imagem(tema, i));
            produto.setImagens(new ArrayList<>(List.of(imagem(tema, i + 100), imagem(tema, i + 200))));
            // Dois anúncios pausados, para demonstrar a diferença entre a vitrine e "meus produtos".
            produto.setAtivo(i != 13 && i != 37);
            produto.setCategoria(categorias.get((i - 1) % categorias.size()));
            produto.setVendedor(vendedores.get((i - 1) % VENDEDORES_COM_PRODUTOS));
            produtos.add(produtoRepository.save(produto));
        }
        return produtos;
    }

    private List<Endereco> criarEnderecos(List<Cliente> clientes) {
        String[] ruas = {
                "Rua das Flores", "Av. Brasil", "Rua do Comércio", "Alameda Santos",
                "Av. Paulista", "Rua XV de Novembro", "Rua Sete de Setembro",
                "Av. Getúlio Vargas", "Rua Tiradentes", "Rua da Paz"
        };
        String[] cidades = {
                "Campina Grande", "João Pessoa", "Recife", "Natal",
                "Fortaleza", "Maceió", "Aracaju", "Salvador", "Teresina", "São Luís"
        };
        String[] estados = {"PB", "PB", "PE", "RN", "CE", "AL", "SE", "BA", "PI", "MA"};

        List<Endereco> enderecos = new ArrayList<>();
        for (int i = 1; i <= QUANTIDADE; i++) {
            int cidade = (i - 1) % cidades.length;
            Endereco endereco = new Endereco();
            endereco.setRua(ruas[(i - 1) % ruas.length] + ", " + i);
            endereco.setNumero(String.valueOf(100 + i));
            endereco.setComplemento(i % 2 == 0 ? "Apto " + i : null);
            endereco.setCep(String.format("%05d-%03d", 58400 + i, i % 1000));
            endereco.setCidade(cidades[cidade]);
            endereco.setEstado(estados[cidade]);
            endereco.setCliente(clientes.get(i - 1));
            enderecos.add(enderecoRepository.save(endereco));
        }
        return enderecos;
    }

    private List<Cupom> criarCupons() {
        List<Cupom> cupons = new ArrayList<>();
        for (int i = 1; i <= QUANTIDADE; i++) {
            Cupom cupom = new Cupom();
            cupom.setCodigo("CUPOM" + String.format("%02d", i));
            cupom.setValorDesconto(BigDecimal.valueOf(5 + (i % 10) * 10L));
            cupom.setDataExpiracao(LocalDate.now().plusMonths(1 + (i % 12)));
            cupom.setStatus(i <= 40 ? StatusCupom.ATIVO : StatusCupom.INATIVO);
            cupons.add(cupomRepository.save(cupom));
        }
        return cupons;
    }

    private void criarCarrinhos(List<Cliente> clientes, List<Produto> produtos) {
        for (int i = 1; i <= QUANTIDADE; i++) {
            Carrinho carrinho = new Carrinho();
            carrinho.setCliente(clientes.get(i - 1));
            carrinho.setDataCriacao(LocalDate.now().minusDays(i));
            carrinho = carrinhoRepository.save(carrinho);

            Produto produto = produtos.get((i + 9) % produtos.size());
            ItemCarrinho item = new ItemCarrinho();
            item.setId(new ItemCarrinhoId(carrinho.getId(), produto.getId()));
            item.setCarrinho(carrinho);
            item.setProduto(produto);
            item.setQuantidade(1 + (i % 3));
            item.setPrecoUnitario(produto.getPreco());
            carrinho.getItens().add(item);
            carrinhoRepository.save(carrinho);
        }
    }

    /**
     * Cada cliente faz uma compra. Compras de número par têm produtos de dois vendedores e por isso
     * geram dois pedidos, como no checkout real. O cenário (i % 10) define até onde o pedido avançou.
     */
    private void criarCompras(List<Cliente> clientes, List<Endereco> enderecos, List<Produto> produtos, List<Cupom> cupons) {
        MetodoPagamento[] metodos = MetodoPagamento.values();
        String[] transportadoras = {"Correios", "JadLog", "Latam Cargo", "Azul Cargo", "Loggi"};
        String[] comentarios = {
                "Excelente produto! Muito rápido e potente.",
                "Boa qualidade, mas o tamanho veio um pouco diferente.",
                "Produto conforme descrito. Entrega rápida!",
                "Custo-benefício ótimo, recomendo.",
                "Material durável, chegou bem embalado."
        };
        String[] motivos = {
                "Produto com defeito de fábrica",
                "Recebi o produto errado",
                "Produto diferente da foto"
        };

        for (int i = 1; i <= QUANTIDADE; i++) {
            Cliente cliente = clientes.get(i - 1);
            LocalDateTime data = LocalDateTime.now().minusDays(i).withHour(10).withMinute(0).withSecond(0).withNano(0);
            int cenario = i % 10;
            boolean aguardandoPagamento = cenario == 1 || cenario == 6;
            MetodoPagamento metodo = aguardandoPagamento
                    ? (cenario == 1 ? MetodoPagamento.PIX : MetodoPagamento.BOLETO)
                    : metodos[i % metodos.length];
            Cupom cupom = i % 3 == 0 ? cupons.get((i - 1) % 40) : null;

            List<Produto> produtosDaCompra = new ArrayList<>(List.of(produtos.get((i - 1) % produtos.size())));
            List<Integer> quantidades = new ArrayList<>(List.of(1 + (i % 3)));
            if (i % 2 == 0) {
                produtosDaCompra.add(produtos.get(i % produtos.size()));
                quantidades.add(1);
            }

            BigDecimal subtotalCompra = BigDecimal.ZERO;
            for (int p = 0; p < produtosDaCompra.size(); p++) {
                subtotalCompra = subtotalCompra.add(produtosDaCompra.get(p).getPreco().multiply(BigDecimal.valueOf(quantidades.get(p))));
            }
            BigDecimal descontoCompra = cupom != null ? cupom.getValorDesconto().min(subtotalCompra) : BigDecimal.ZERO;

            Compra compra = new Compra();
            compra.setCodigo(String.format("CPSEED%04d", i));
            compra.setCliente(cliente);
            compra.setCupom(cupom);
            compra.setMetodoPagamento(metodo);
            compra.setDataCompra(data);
            compra.setValorSubtotal(subtotalCompra);
            compra.setValorDesconto(descontoCompra);
            compra = compraRepository.save(compra);

            BigDecimal descontoDistribuido = BigDecimal.ZERO;
            BigDecimal totalCompra = BigDecimal.ZERO;
            for (int p = 0; p < produtosDaCompra.size(); p++) {
                Produto produto = produtosDaCompra.get(p);
                int quantidade = quantidades.get(p);
                boolean ultimo = p == produtosDaCompra.size() - 1;
                BigDecimal subtotalPedido = produto.getPreco().multiply(BigDecimal.valueOf(quantidade));
                BigDecimal descontoPedido = ultimo
                        ? descontoCompra.subtract(descontoDistribuido)
                        : descontoCompra.multiply(subtotalPedido).divide(subtotalCompra, 2, RoundingMode.HALF_UP);
                descontoDistribuido = descontoDistribuido.add(descontoPedido);

                Pedido pedido = new Pedido();
                pedido.setCompra(compra);
                pedido.setCliente(cliente);
                pedido.setCupom(cupom);
                pedido.setDataPedido(data);
                pedido.setEnderecoEntrega(EnderecoEntrega.de(enderecos.get(i - 1)));
                pedido.setValorDesconto(descontoPedido);
                pedido.setStatus(StatusPedido.CONFIRMADO);
                pedido = pedidoRepository.save(pedido);
                adicionarItem(pedido, produto, quantidade);
                pedido.recalcularValorTotal();
                historico(pedido, EtapaPedido.PEDIDO_CONFIRMADO, data, "Pedido realizado na compra " + compra.getCodigo() + ".", cliente);

                String transportadora = transportadoras[i % transportadoras.length];
                avancarPedido(pedido, cenario, metodo, data, transportadora, cliente, produto.getVendedor(), motivos[i % motivos.length]);
                pedido = pedidoRepository.save(pedido);
                compra.getPedidos().add(pedido);
                totalCompra = totalCompra.add(pedido.getValorTotal());

                if (p == 0 && (cenario == 3 || cenario == 8)) {
                    Avaliacao avaliacao = new Avaliacao();
                    avaliacao.setCliente(cliente);
                    avaliacao.setProduto(produto);
                    avaliacao.setNota(1 + (i % 5));
                    avaliacao.setComentario(comentarios[i % comentarios.length]);
                    avaliacao.setDataAvaliacao(data.plusDays(8));
                    avaliacaoRepository.save(avaliacao);
                }
            }
            compra.setValorTotal(totalCompra);
            compraRepository.save(compra);
        }
    }

    private void avancarPedido(Pedido pedido, int cenario, MetodoPagamento metodo, LocalDateTime data,
                               String transportadora, Cliente cliente, Vendedor vendedor, String motivo) {
        boolean cancelado = cenario == 0;
        boolean aguardandoPagamento = cenario == 1 || cenario == 6;

        Pagamento pagamento = criarPagamento(pedido, metodo,
                cancelado ? StatusPagamento.RECUSADO : aguardandoPagamento ? StatusPagamento.PENDENTE : StatusPagamento.APROVADO,
                data);

        if (cancelado) {
            pedido.setStatus(StatusPedido.CANCELADO);
            historico(pedido, EtapaPedido.CANCELADO, data.plusHours(2), "Pedido cancelado pelo cliente.", cliente);
            return;
        }
        baixarEstoque(pedido);
        if (aguardandoPagamento) {
            return;
        }
        historico(pedido, EtapaPedido.PAGAMENTO_APROVADO, data.plusHours(1), "Pagamento via " + metodo + " confirmado.", vendedor);
        if (cenario == 5) {
            return;
        }

        StatusEntrega statusFinal = switch (cenario) {
            case 2 -> StatusEntrega.EMBALADO;
            case 7 -> StatusEntrega.EM_TRANSITO;
            case 4 -> StatusEntrega.SAIU_PARA_ENTREGA;
            default -> StatusEntrega.ENTREGUE;
        };
        Entrega entrega = new Entrega();
        entrega.setPedido(pedido);
        entrega.setTransportadora(transportadora);
        entrega.setCodigoRastreamento("BR" + String.format("%09d", pedido.getId()) + "BR");
        entrega.setStatusEntrega(statusFinal);
        entrega.setDataEntregaPrevista(data.plusDays(9));
        historico(pedido, EtapaPedido.EMBALADO, data.plusDays(1), "Pedido embalado e aguardando envio.", vendedor);
        if (statusFinal.ordinal() >= StatusEntrega.EM_TRANSITO.ordinal()) {
            entrega.setDataEnvio(data.plusDays(2));
            historico(pedido, EtapaPedido.EM_TRANSITO, data.plusDays(2), "Pedido enviado pela " + transportadora
                    + ". Código de rastreamento: " + entrega.getCodigoRastreamento() + ".", vendedor);
        }
        if (statusFinal.ordinal() >= StatusEntrega.SAIU_PARA_ENTREGA.ordinal()) {
            historico(pedido, EtapaPedido.SAIU_PARA_ENTREGA, data.plusDays(5), "Pedido saiu para entrega.", vendedor);
        }
        if (statusFinal == StatusEntrega.ENTREGUE) {
            entrega.setDataEntrega(data.plusDays(6));
            historico(pedido, EtapaPedido.ENTREGUE, data.plusDays(6), "Pedido entregue.", vendedor);
        }
        pedido.setEntrega(entregaRepository.save(entrega));

        if (cenario == 8 || cenario == 9) {
            Devolucao devolucao = new Devolucao();
            devolucao.setPedido(pedido);
            devolucao.setMotivo(motivo);
            devolucao.setStatus(cenario == 9 ? StatusDevolucao.APROVADA : StatusDevolucao.PENDENTE);
            devolucao.setDataDevolucao(data.plusDays(9));
            devolucaoRepository.save(devolucao);
            historico(pedido, EtapaPedido.DEVOLUCAO_SOLICITADA, data.plusDays(9), "Devolução solicitada: " + motivo, cliente);
        }
        if (cenario == 9) {
            pedido.getItens().forEach(item ->
                    item.getProduto().setEstoque(item.getProduto().getEstoque() + item.getQuantidade()));
            pedido.setStatus(StatusPedido.DEVOLVIDO);
            pagamento.setStatus(StatusPagamento.ESTORNADO);
            pagamentoRepository.save(pagamento);
            historico(pedido, EtapaPedido.DEVOLVIDO, data.plusDays(10), "Devolução aprovada e pagamento estornado.", vendedor);
        }
    }

    private void baixarEstoque(Pedido pedido) {
        pedido.getItens().forEach(item ->
                item.getProduto().setEstoque(Math.max(0, item.getProduto().getEstoque() - item.getQuantidade())));
    }

    private void adicionarItem(Pedido pedido, Produto produto, int quantidade) {
        ItemPedido item = new ItemPedido();
        item.setId(new ItemPedidoId(pedido.getId(), produto.getId()));
        item.setPedido(pedido);
        item.setProduto(produto);
        item.setQuantidade(quantidade);
        item.setPrecoUnitario(produto.getPreco());
        pedido.getItens().add(item);
    }

    private Pagamento criarPagamento(Pedido pedido, MetodoPagamento metodo, StatusPagamento status, LocalDateTime dataPedido) {
        Pagamento pagamento = new Pagamento();
        pagamento.setPedido(pedido);
        pagamento.setMetodo(metodo);
        pagamento.setStatus(status);
        pagamento.setValorPago(pedido.getValorTotal());
        pagamento.setDataPagamento(dataPedido.plusHours(1));
        pagamento = pagamentoRepository.save(pagamento);
        pedido.setPagamento(pagamento);
        return pagamento;
    }

    private void historico(Pedido pedido, EtapaPedido etapa, LocalDateTime data, String descricao, Usuario autor) {
        PedidoHistorico registro = new PedidoHistorico();
        registro.setPedido(pedido);
        registro.setEtapa(etapa);
        registro.setData(data);
        registro.setDescricao(descricao);
        PedidoHistoricoService.preencherAutor(registro, autor);
        pedido.getHistorico().add(registro);
    }

    private String imagem(String tema, int seed) {
        return "https://loremflickr.com/640/640/" + tema + "?lock=" + seed;
    }

    private String semAcentos(String texto) {
        return Normalizer.normalize(texto, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
    }
}
