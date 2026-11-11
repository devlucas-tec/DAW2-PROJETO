package br.edu.ifpb.es.daw;

import br.edu.ifpb.es.daw.model.*;
import br.edu.ifpb.es.daw.model.enums.Role;
import br.edu.ifpb.es.daw.repository.*;
import com.jayway.jsonpath.JsonPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Base dos testes de integração: sobe a aplicação inteira com H2 e cria os dados direto pelos repositórios.
 * Cada teste cria os próprios usuários/produtos com e-mails únicos, então os testes não dependem uns dos outros.
 */
@SpringBootTest
@AutoConfigureMockMvc
public abstract class IntegracaoTestBase {

    protected static final String SENHA = "senha123";
    private static final AtomicInteger SEQUENCIA = new AtomicInteger();

    @Autowired protected MockMvc mvc;
    @Autowired protected PasswordEncoder passwordEncoder;
    @Autowired protected ClienteRepository clienteRepository;
    @Autowired protected VendedorRepository vendedorRepository;
    @Autowired protected AdminRepository adminRepository;
    @Autowired protected CategoriaRepository categoriaRepository;
    @Autowired protected ProdutoRepository produtoRepository;
    @Autowired protected EnderecoRepository enderecoRepository;
    @Autowired protected CupomRepository cupomRepository;
    @Autowired protected PedidoRepository pedidoRepository;
    @Autowired protected CompraRepository compraRepository;
    @Autowired protected CarrinhoRepository carrinhoRepository;

    protected static int proximo() {
        return SEQUENCIA.incrementAndGet();
    }

    // ---------- Criação de dados ----------

    protected Cliente criarCliente() {
        int n = proximo();
        Cliente cliente = new Cliente();
        cliente.setNome("Cliente Teste " + n);
        cliente.setEmail("cliente.teste" + n + "@email.com");
        cliente.setSenha(passwordEncoder.encode(SENHA));
        cliente.setRole(Role.CLIENTE);
        return clienteRepository.save(cliente);
    }

    protected Vendedor criarVendedor() {
        int n = proximo();
        Vendedor vendedor = new Vendedor();
        vendedor.setNome("Vendedor Teste " + n);
        vendedor.setEmail("vendedor.teste" + n + "@loja.com");
        vendedor.setSenha(passwordEncoder.encode(SENHA));
        vendedor.setRole(Role.VENDEDOR);
        vendedor.setRazaoSocial("Loja Teste " + n + " Ltda");
        vendedor.setCnpjCpf(String.format("99.%03d.%03d/0001-00", n % 1000, n / 1000));
        return vendedorRepository.save(vendedor);
    }

    protected Admin criarAdmin() {
        int n = proximo();
        Admin admin = new Admin();
        admin.setNome("Admin Teste " + n);
        admin.setEmail("admin.teste" + n + "@sistema.com");
        admin.setSenha(passwordEncoder.encode(SENHA));
        admin.setRole(Role.ADMIN);
        return adminRepository.save(admin);
    }

    protected Categoria criarCategoria() {
        Categoria categoria = new Categoria();
        categoria.setNome("Categoria Teste " + proximo());
        return categoriaRepository.save(categoria);
    }

    protected Produto criarProduto(Vendedor vendedor, String preco, int estoque) {
        return criarProduto(vendedor, preco, estoque, true);
    }

    protected Produto criarProduto(Vendedor vendedor, String preco, int estoque, boolean ativo) {
        Produto produto = new Produto();
        produto.setNome("Produto Teste " + proximo());
        produto.setPreco(new BigDecimal(preco));
        produto.setEstoque(estoque);
        produto.setAtivo(ativo);
        produto.setImagemUrl("https://loremflickr.com/640/640/product?lock=1");
        produto.setVendedor(vendedor);
        produto.setCategoria(criarCategoria());
        return produtoRepository.save(produto);
    }

    protected Endereco criarEndereco(Cliente cliente) {
        Endereco endereco = new Endereco();
        endereco.setRua("Rua dos Testes");
        endereco.setNumero("100");
        endereco.setCep("58400-000");
        endereco.setCidade("Campina Grande");
        endereco.setEstado("PB");
        endereco.setCliente(cliente);
        return enderecoRepository.save(endereco);
    }

    // ---------- Autenticação e requisições ----------

    protected String login(Usuario usuario) throws Exception {
        MvcResult resultado = mvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json("{'email':'%s','senha':'%s'}", usuario.getEmail(), SENHA)))
                .andExpect(status().isOk())
                .andReturn();
        return JsonPath.read(corpo(resultado), "$.token");
    }

    /** Executa a requisição com o token informado (ou sem token, se for null). */
    protected ResultActions executar(MockHttpServletRequestBuilder requisicao, String token) throws Exception {
        if (token != null) {
            requisicao.header("Authorization", "Bearer " + token);
        }
        return mvc.perform(requisicao);
    }

    protected ResultActions executar(MockHttpServletRequestBuilder requisicao, String token, String corpoJson) throws Exception {
        return executar(requisicao.contentType(MediaType.APPLICATION_JSON).content(corpoJson), token);
    }

    /** Monta JSON usando aspas simples no template para facilitar a leitura dos testes. */
    protected static String json(String template, Object... args) {
        return String.format(template, args).replace('\'', '"');
    }

    protected static String corpo(MvcResult resultado) throws Exception {
        return resultado.getResponse().getContentAsString(StandardCharsets.UTF_8);
    }

    protected static <T> T ler(ResultActions resultado, String caminho) throws Exception {
        return JsonPath.read(corpo(resultado.andReturn()), caminho);
    }
}
