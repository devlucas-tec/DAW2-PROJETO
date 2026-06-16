package br.edu.ifpb.es.daw.seed;

import br.edu.ifpb.es.daw.model.*;
import br.edu.ifpb.es.daw.model.enums.*;
import br.edu.ifpb.es.daw.repository.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
            PasswordEncoder passwordEncoder) {

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
    }

    @Transactional
    public void limpar() {
        avaliacaoRepository.deleteAll();
        devolucaoRepository.deleteAll();
        entregaRepository.deleteAll();
        pagamentoRepository.deleteAll();
        itemPedidoRepository.deleteAll();
        pedidoRepository.deleteAll();
        itemCarrinhoRepository.deleteAll();
        carrinhoRepository.deleteAll();
        enderecoRepository.deleteAll();
        cupomRepository.deleteAll();
        produtoRepository.deleteAll();
        categoriaRepository.deleteAll();
        clienteRepository.deleteAll();
        vendedorRepository.deleteAll();
        adminRepository.deleteAll();
    }

    @Transactional
    public void popular() {

        // ---------------------------------------------------------------
        // ADMINS (50)
        // ---------------------------------------------------------------
        for (int i = 1; i <= QUANTIDADE; i++) {
            Admin admin = new Admin();
            admin.setNome("Administrador " + i);
            admin.setEmail("admin" + i + "@sistema.com");
            admin.setSenha(passwordEncoder.encode("admin123"));
            admin.setRole(Role.ADMIN);
            adminRepository.save(admin);
        }

        // ---------------------------------------------------------------
        // VENDEDORES (50)
        // ---------------------------------------------------------------
        List<Vendedor> vendedores = new ArrayList<>();

        String[] nomesVendedor = {
                "Tech Store", "Modas", "Eletro Shop", "Casa & Cia", "Pet Center",
                "Livros & Cia", "Sports Zone", "Gamer Zone", "Farmácia +", "Auto Peças"
        };
        String[] segmentos = {
                "Eletrônicos", "Moda", "Eletrodomésticos", "Casa", "Pets",
                "Livros", "Esportes", "Games", "Saúde", "Automotivo"
        };

        for (int i = 1; i <= QUANTIDADE; i++) {
            String segmento = segmentos[(i - 1) % segmentos.length];
            String nomeBase = nomesVendedor[(i - 1) % nomesVendedor.length];

            Vendedor vendedor = new Vendedor();
            vendedor.setNome(nomeBase + " " + i);
            vendedor.setEmail("vendedor" + i + "@" + segmento.toLowerCase().replace(" ", "") + ".com");
            vendedor.setSenha(passwordEncoder.encode("senha123"));
            vendedor.setRole(Role.VENDEDOR);
            vendedor.setRazaoSocial(nomeBase + " " + i + " Ltda");
            vendedor.setCnpjCpf(String.format("%02d.%03d.%03d/0001-%02d", i, i * 3, i * 7, i % 99 + 1));
            vendedores.add(vendedorRepository.save(vendedor));
        }

        // ---------------------------------------------------------------
        // CLIENTES (50)
        // ---------------------------------------------------------------
        List<Cliente> clientes = new ArrayList<>();

        String[] nomesCliente = {
                "Alex", "Maria", "João", "Ana", "Pedro",
                "Lucia", "Carlos", "Fernanda", "Rafael", "Juliana"
        };
        String[] sobrenomes = {
                "Pereira", "Silva", "Santos", "Oliveira", "Costa",
                "Souza", "Lima", "Alves", "Rocha", "Gomes"
        };

        for (int i = 1; i <= QUANTIDADE; i++) {
            String nome = nomesCliente[(i - 1) % nomesCliente.length];
            String sobrenome = sobrenomes[(i - 1) % sobrenomes.length];

            Cliente cliente = new Cliente();
            cliente.setNome(nome + " " + sobrenome + " " + i);
            cliente.setEmail("cliente" + i + "@email.com");
            cliente.setSenha(passwordEncoder.encode("senha123"));
            cliente.setRole(Role.CLIENTE);
            cliente.setTelefone("839999" + String.format("%05d", i));
            clientes.add(clienteRepository.save(cliente));
        }

        // ---------------------------------------------------------------
        // CATEGORIAS (50)
        // ---------------------------------------------------------------
        List<Categoria> categorias = new ArrayList<>();

        String[] nomesCategoria = {
                "Eletrônicos", "Roupas", "Calçados", "Informática", "Games",
                "Livros", "Esportes", "Casa", "Jardim", "Pets",
                "Beleza", "Saúde", "Automotivo", "Bebês", "Brinquedos",
                "Ferramentas", "Papelaria", "Música", "Filmes", "Alimentos"
        };

        for (int i = 1; i <= QUANTIDADE; i++) {
            String nomeBase = nomesCategoria[(i - 1) % nomesCategoria.length];

            Categoria categoria = new Categoria();
            categoria.setNome(nomeBase + " " + i);
            categoria.setDescricao("Categoria de " + nomeBase.toLowerCase() + " - linha " + i);
            categorias.add(categoriaRepository.save(categoria));
        }

        // ---------------------------------------------------------------
        // PRODUTOS (50)
        // ---------------------------------------------------------------
        List<Produto> produtos = new ArrayList<>();

        String[] nomesProduto = {
                "Notebook Gamer", "Fone Bluetooth", "Camiseta Básica", "Tênis Running",
                "Mouse Sem Fio", "Teclado Mecânico", "Monitor 24\"", "SSD 1TB",
                "Cadeira Gamer", "Headset Pro", "Smartphone", "Tablet", "Smartwatch",
                "Câmera Digital", "Impressora", "Roteador Wi-Fi", "HD Externo",
                "Pendrive 64GB", "Webcam HD", "Caixa de Som"
        };

        for (int i = 1; i <= QUANTIDADE; i++) {
            String nomeProd = nomesProduto[(i - 1) % nomesProduto.length];
            Categoria categoria = categorias.get((i - 1) % categorias.size());
            Vendedor vendedor = vendedores.get((i - 1) % vendedores.size());

            BigDecimal preco = new BigDecimal("99.90")
                    .add(new BigDecimal(i * 50L));

            Produto produto = new Produto();
            produto.setNome(nomeProd + " " + i);
            produto.setDescricao("Descrição detalhada do produto: " + nomeProd + " - modelo " + i);
            produto.setPreco(preco);
            produto.setEstoque(10 + (i % 100));
            produto.setCategoria(categoria);
            produto.setVendedor(vendedor);
            produtos.add(produtoRepository.save(produto));
        }

        // ---------------------------------------------------------------
        // ENDEREÇOS (50) — um por cliente
        // ---------------------------------------------------------------
        String[] ruas = {
                "Rua das Flores", "Av. Brasil", "Rua do Comércio", "Alameda Santos",
                "Av. Paulista", "Rua XV de Novembro", "Rua Sete de Setembro",
                "Av. Getúlio Vargas", "Rua Tiradentes", "Rua da Paz"
        };
        String[] cidades = {
                "Campina Grande", "João Pessoa", "Recife", "Natal",
                "Fortaleza", "Maceió", "Aracaju", "Salvador", "Teresina", "São Luís"
        };
        String[] estados = { "PB", "PB", "PE", "RN", "CE", "AL", "SE", "BA", "PI", "MA" };

        for (int i = 1; i <= QUANTIDADE; i++) {
            Cliente cliente = clientes.get(i - 1);
            String rua = ruas[(i - 1) % ruas.length];
            int cidadeIdx = (i - 1) % cidades.length;

            Endereco endereco = new Endereco();
            endereco.setRua(rua + ", " + i);
            endereco.setNumero(String.valueOf(100 + i));
            endereco.setComplemento(i % 2 == 0 ? "Apto " + i : null);
            endereco.setCep(String.format("%05d-%03d", 58400 + i, i % 1000));
            endereco.setCidade(cidades[cidadeIdx]);
            endereco.setEstado(estados[cidadeIdx]);
            endereco.setCliente(cliente);
            enderecoRepository.save(endereco);
        }

        // ---------------------------------------------------------------
        // CUPONS (50)
        // ---------------------------------------------------------------
        List<Cupom> cupons = new ArrayList<>();

        StatusCupom[] statusCupom = StatusCupom.values();

        for (int i = 1; i <= QUANTIDADE; i++) {
            BigDecimal desconto = new BigDecimal(5 + (i % 10) * 10L);

            Cupom cupom = new Cupom();
            cupom.setCodigo("CUPOM" + String.format("%02d", i));
            cupom.setValorDesconto(desconto);
            cupom.setDataExpiracao(LocalDate.now().plusMonths(1 + (i % 12)));
            cupom.setStatus(i <= 40 ? StatusCupom.ATIVO : statusCupom[i % statusCupom.length]);
            cupons.add(cupomRepository.save(cupom));
        }

        // ---------------------------------------------------------------
        // CARRINHOS + ITENS DE CARRINHO (50 cada)
        // ---------------------------------------------------------------
        List<Carrinho> carrinhos = new ArrayList<>();

        for (int i = 1; i <= QUANTIDADE; i++) {
            Cliente cliente = clientes.get((i - 1) % clientes.size());

            Carrinho carrinho = new Carrinho();
            carrinho.setCliente(cliente);
            carrinho.setDataCriacao(LocalDate.now().minusDays(i));
            carrinho.setDataAtualizacao(LocalDate.now());
            carrinhos.add(carrinhoRepository.save(carrinho));
        }

        for (int i = 1; i <= QUANTIDADE; i++) {
            Carrinho carrinho = carrinhos.get(i - 1);
            Produto produto = produtos.get((i - 1) % produtos.size());

            ItemCarrinhoId icId = new ItemCarrinhoId(carrinho.getId(), produto.getId());
            ItemCarrinho itemCarrinho = new ItemCarrinho();
            itemCarrinho.setId(icId);
            itemCarrinho.setCarrinho(carrinho);
            itemCarrinho.setProduto(produto);
            itemCarrinho.setQuantidade(1 + (i % 5));
            itemCarrinho.setPrecoUnitario(produto.getPreco());
            itemCarrinhoRepository.save(itemCarrinho);
        }

        // ---------------------------------------------------------------
        // PEDIDOS + ITENS DE PEDIDO (50 cada)
        // ---------------------------------------------------------------
        List<Pedido> pedidos = new ArrayList<>();

        StatusPedido[] statusPedidos = StatusPedido.values();

        for (int i = 1; i <= QUANTIDADE; i++) {
            Cliente cliente = clientes.get((i - 1) % clientes.size());
            Cupom cupom = (i % 3 == 0) ? cupons.get((i - 1) % cupons.size()) : null;
            BigDecimal valorTotal = new BigDecimal("199.90").add(new BigDecimal(i * 30L));

            Pedido pedido = new Pedido();
            pedido.setCliente(cliente);
            pedido.setCupom(cupom);
            pedido.setValorTotal(valorTotal);
            pedido.setStatus(statusPedidos[(i - 1) % statusPedidos.length]);
            pedido.setDataPedido(LocalDateTime.now().minusDays(i));
            pedidos.add(pedidoRepository.save(pedido));
        }

        for (int i = 1; i <= QUANTIDADE; i++) {
            Pedido pedido = pedidos.get(i - 1);
            Produto produto = produtos.get((i - 1) % produtos.size());

            ItemPedidoId ipId = new ItemPedidoId(pedido.getId(), produto.getId());
            ItemPedido itemPedido = new ItemPedido();
            itemPedido.setId(ipId);
            itemPedido.setPedido(pedido);
            itemPedido.setProduto(produto);
            itemPedido.setQuantidade(1 + (i % 4));
            itemPedido.setPrecoUnitario(produto.getPreco());
            itemPedidoRepository.save(itemPedido);
        }

        // ---------------------------------------------------------------
        // PAGAMENTOS (50)
        // ---------------------------------------------------------------
        MetodoPagamento[] metodos = MetodoPagamento.values();
        StatusPagamento[] statusPagamentos = StatusPagamento.values();

        for (int i = 1; i <= QUANTIDADE; i++) {
            Pedido pedido = pedidos.get(i - 1);

            Pagamento pagamento = new Pagamento();
            pagamento.setPedido(pedido);
            pagamento.setMetodo(metodos[(i - 1) % metodos.length]);
            pagamento.setStatus(statusPagamentos[(i - 1) % statusPagamentos.length]);
            pagamento.setValorPago(pedido.getValorTotal());
            pagamento.setDataPagamento(LocalDateTime.now().minusDays(i));
            pagamentoRepository.save(pagamento);
        }

        // ---------------------------------------------------------------
        // ENTREGAS (50)
        // ---------------------------------------------------------------
        StatusEntrega[] statusEntregas = StatusEntrega.values();
        String[] transportadoras = { "Correios", "JadLog", "Latam Cargo", "Azul Cargo", "Loggi" };

        for (int i = 1; i <= QUANTIDADE; i++) {
            Pedido pedido = pedidos.get(i - 1);

            Entrega entrega = new Entrega();
            entrega.setPedido(pedido);
            entrega.setTransportadora(transportadoras[(i - 1) % transportadoras.length]);
            entrega.setCodigoRastreamento("BR" + String.format("%09d", i) + "BR");
            entrega.setStatusEntrega(statusEntregas[(i - 1) % statusEntregas.length]);
            entrega.setDataEnvio(LocalDateTime.now().minusDays(i));
            entrega.setDataEntregaPrevista(LocalDateTime.now().plusDays(3 + (i % 7)));
            entregaRepository.save(entrega);
        }

        // ---------------------------------------------------------------
        // DEVOLUÇÕES (50)
        // ---------------------------------------------------------------
        StatusDevolucao[] statusDevolucoes = StatusDevolucao.values();
        String[] motivos = {
                "Produto com defeito de fábrica",
                "Tamanho diferente do pedido",
                "Produto chegou avariado",
                "Não gostei do produto",
                "Recebi o produto errado",
                "Qualidade inferior ao anunciado",
                "Produto diferente da foto",
                "Desisti da compra",
                "Duplicidade no pedido",
                "Presente indesejado"
        };

        for (int i = 1; i <= QUANTIDADE; i++) {
            Pedido pedido = pedidos.get(i - 1);

            Devolucao devolucao = new Devolucao();
            devolucao.setPedido(pedido);
            devolucao.setMotivo(motivos[(i - 1) % motivos.length]);
            devolucao.setStatus(statusDevolucoes[(i - 1) % statusDevolucoes.length]);
            devolucao.setDataDevolucao(LocalDateTime.now().minusDays(i % 30));
            devolucaoRepository.save(devolucao);
        }

        // ---------------------------------------------------------------
        // AVALIAÇÕES (50)
        // ---------------------------------------------------------------
        String[] comentarios = {
                "Excelente produto! Muito rápido e potente.",
                "Boa qualidade, mas o tamanho veio um pouco diferente.",
                "Produto conforme descrito. Entrega rápida!",
                "Custo-benefício ótimo, recomendo.",
                "Material durável, chegou bem embalado.",
                "Superou as expectativas. Nota máxima!",
                "Produto bom, mas poderia ter manual em português.",
                "Entrega atrasou, mas o produto é de qualidade.",
                "Exatamente o que eu precisava. Parabéns!",
                "Produto ok, nada além do esperado."
        };

        for (int i = 1; i <= QUANTIDADE; i++) {
            Cliente cliente = clientes.get((i - 1) % clientes.size());
            Produto produto = produtos.get((i - 1) % produtos.size());

            Avaliacao avaliacao = new Avaliacao();
            avaliacao.setCliente(cliente);
            avaliacao.setProduto(produto);
            avaliacao.setNota(1 + (i % 5));
            avaliacao.setComentario(comentarios[(i - 1) % comentarios.length]);
            avaliacao.setDataAvaliacao(LocalDateTime.now().minusDays(i % 30));
            avaliacaoRepository.save(avaliacao);
        }
    }
}