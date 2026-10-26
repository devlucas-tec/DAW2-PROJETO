package br.edu.ifpb.es.daw.seed;

import br.edu.ifpb.es.daw.config.StartupListener;
import br.edu.ifpb.es.daw.exception.EstadoInvalidoException;
import br.edu.ifpb.es.daw.model.*;
import br.edu.ifpb.es.daw.model.enums.*;
import br.edu.ifpb.es.daw.repository.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SeedService {

    private static final int QUANTIDADE = 50;

    private final AdminRepository adminRepository;
    private final ClienteRepository clienteRepository;
    private final VendedorRepository vendedorRepository;
    private final CategoriaRepository categoriaRepository;
    private final ProdutoRepository produtoRepository;
    private final EnderecoRepository enderecoRepository;
    private final CupomRepository cupomRepository;
    private final CarrinhoRepository carrinhoRepository;
    private final ItemCarrinhoRepository itemCarrinhoRepository;
    private final PedidoRepository pedidoRepository;
    private final ItemPedidoRepository itemPedidoRepository;
    private final PagamentoRepository pagamentoRepository;
    private final EntregaRepository entregaRepository;
    private final DevolucaoRepository devolucaoRepository;
    private final AvaliacaoRepository avaliacaoRepository;
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
            PedidoRepository pedidoRepository,
            ItemPedidoRepository itemPedidoRepository,
            PagamentoRepository pagamentoRepository,
            EntregaRepository entregaRepository,
            DevolucaoRepository devolucaoRepository,
            AvaliacaoRepository avaliacaoRepository,
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
        this.pedidoRepository = pedidoRepository;
        this.itemPedidoRepository = itemPedidoRepository;
        this.pagamentoRepository = pagamentoRepository;
        this.entregaRepository = entregaRepository;
        this.devolucaoRepository = devolucaoRepository;
        this.avaliacaoRepository = avaliacaoRepository;
        this.passwordEncoder = passwordEncoder;
        this.startupListener = startupListener;
    }

    @Transactional
    public void limpar() {
        avaliacaoRepository.deleteAllInBatch();
        devolucaoRepository.deleteAllInBatch();
        entregaRepository.deleteAllInBatch();
        pagamentoRepository.deleteAllInBatch();
        itemPedidoRepository.deleteAllInBatch();
        pedidoRepository.deleteAllInBatch();
        itemCarrinhoRepository.deleteAllInBatch();
        carrinhoRepository.deleteAllInBatch();
        enderecoRepository.deleteAllInBatch();
        cupomRepository.deleteAllInBatch();
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
        criarEnderecos(clientes);
        List<Cupom> cupons = criarCupons();
        criarCarrinhos(clientes, produtos);
        criarPedidos(clientes, produtos, cupons);
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

        List<Produto> produtos = new ArrayList<>();
        for (int i = 1; i <= QUANTIDADE; i++) {
            String nomeProduto = nomesProduto[(i - 1) % nomesProduto.length];
            Produto produto = new Produto();
            produto.setNome(nomeProduto + " " + i);
            produto.setDescricao("Descrição detalhada do produto: " + nomeProduto + " - modelo " + i);
            produto.setPreco(new BigDecimal("99.90").add(BigDecimal.valueOf(i * 50L)));
            produto.setEstoque(20 + i);
            produto.setCategoria(categorias.get((i - 1) % categorias.size()));
            produto.setVendedor(vendedores.get((i - 1) % vendedores.size()));
            produtos.add(produtoRepository.save(produto));
        }
        return produtos;
    }

    private void criarEnderecos(List<Cliente> clientes) {
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
            enderecoRepository.save(endereco);
        }
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

    private void criarPedidos(List<Cliente> clientes, List<Produto> produtos, List<Cupom> cupons) {
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
            LocalDateTime dataPedido = LocalDateTime.now().minusDays(i);

            Pedido pedido = new Pedido();
            pedido.setCliente(cliente);
            pedido.setDataPedido(dataPedido);
            pedido.setStatus(StatusPedido.CRIADO);
            if (i % 3 == 0) {
                pedido.setCupom(cupons.get((i - 1) % 40));
            }
            pedido = pedidoRepository.save(pedido);

            adicionarItem(pedido, produtos.get((i - 1) % produtos.size()), 1 + (i % 3));
            if (i % 2 == 0) {
                adicionarItem(pedido, produtos.get(i % produtos.size()), 1);
            }
            pedido.recalcularValorTotal();

            int cenario = i % 10;
            if (cenario == 0 || cenario == 5) {
                pedidoRepository.save(pedido);
                continue;
            }

            if (cenario == 4) {
                pedido.setStatus(StatusPedido.CANCELADO);
                pedidoRepository.save(pedido);
                criarPagamento(pedido, metodos[i % metodos.length], StatusPagamento.RECUSADO, dataPedido);
                continue;
            }

            pedido.setStatus(StatusPedido.CONFIRMADO);
            pedido.getItens().forEach(item ->
                    item.getProduto().setEstoque(item.getProduto().getEstoque() - item.getQuantidade()));
            pedidoRepository.save(pedido);

            if (cenario == 1 || cenario == 6) {
                criarPagamento(pedido, metodos[i % metodos.length], StatusPagamento.PENDENTE, dataPedido);
                continue;
            }

            Pagamento pagamento = criarPagamento(pedido, metodos[i % metodos.length], StatusPagamento.APROVADO, dataPedido);
            String transportadora = transportadoras[i % transportadoras.length];

            if (cenario == 2 || cenario == 7) {
                criarEntrega(pedido, transportadora, i, StatusEntrega.EM_TRANSITO, dataPedido);
                continue;
            }

            criarEntrega(pedido, transportadora, i, StatusEntrega.ENTREGUE, dataPedido);
            Produto produtoAvaliado = pedido.getItens().get(0).getProduto();

            if (cenario == 9) {
                Devolucao devolucao = new Devolucao();
                devolucao.setPedido(pedido);
                devolucao.setMotivo(motivos[i % motivos.length]);
                devolucao.setStatus(StatusDevolucao.APROVADA);
                devolucao.setDataDevolucao(dataPedido.plusDays(10));
                devolucaoRepository.save(devolucao);
                pedido.getItens().forEach(item ->
                        item.getProduto().setEstoque(item.getProduto().getEstoque() + item.getQuantidade()));
                pedido.setStatus(StatusPedido.DEVOLVIDO);
                pagamento.setStatus(StatusPagamento.ESTORNADO);
                pedidoRepository.save(pedido);
                pagamentoRepository.save(pagamento);
                continue;
            }

            if (cenario == 8) {
                Devolucao devolucao = new Devolucao();
                devolucao.setPedido(pedido);
                devolucao.setMotivo(motivos[i % motivos.length]);
                devolucao.setStatus(StatusDevolucao.PENDENTE);
                devolucao.setDataDevolucao(dataPedido.plusDays(9));
                devolucaoRepository.save(devolucao);
            }

            Avaliacao avaliacao = new Avaliacao();
            avaliacao.setCliente(cliente);
            avaliacao.setProduto(produtoAvaliado);
            avaliacao.setNota(1 + (i % 5));
            avaliacao.setComentario(comentarios[i % comentarios.length]);
            avaliacao.setDataAvaliacao(dataPedido.plusDays(8));
            avaliacaoRepository.save(avaliacao);
        }
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
        return pagamentoRepository.save(pagamento);
    }

    private void criarEntrega(Pedido pedido, String transportadora, int i, StatusEntrega status, LocalDateTime dataPedido) {
        Entrega entrega = new Entrega();
        entrega.setPedido(pedido);
        entrega.setTransportadora(transportadora);
        entrega.setCodigoRastreamento("BR" + String.format("%09d", i) + "BR");
        entrega.setStatusEntrega(status);
        entrega.setDataEnvio(dataPedido.plusDays(1));
        entrega.setDataEntregaPrevista(dataPedido.plusDays(8));
        if (status == StatusEntrega.ENTREGUE) {
            entrega.setDataEntrega(dataPedido.plusDays(6));
        }
        entregaRepository.save(entrega);
    }

    private String semAcentos(String texto) {
        return Normalizer.normalize(texto, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
    }
}
