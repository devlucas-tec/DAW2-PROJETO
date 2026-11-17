package br.edu.ifpb.es.daw;

import br.edu.ifpb.es.daw.model.*;
import br.edu.ifpb.es.daw.model.enums.StatusCupom;
import br.edu.ifpb.es.daw.repository.AvaliacaoRepository;
import br.edu.ifpb.es.daw.repository.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Suporte do back-end às telas do front (loja, vitrine, checkout, painéis e extras)")
class SuporteFrontTest extends IntegracaoTestBase {

    @Autowired private AvaliacaoRepository avaliacaoRepository;
    @Autowired private UsuarioRepository usuarioRepository;

    // ---------------------------------------------------------------- Loja do vendedor

    @Test
    @DisplayName("Descrição e banner da loja aparecem no perfil público; loja fechada some da vitrine e do destaque")
    void perfilEAberturaDaLoja() throws Exception {
        Vendedor vendedor = criarVendedor();
        Produto produto = criarProduto(vendedor, "50.00", 5);
        String token = login(vendedor);

        executar(patch("/vendedores/" + vendedor.getId()), token, json(
                "{'descricao':'Eletrônicos originais com nota fiscal.','bannerUrl':'https://loremflickr.com/1200/300/store'}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descricao").value("Eletrônicos originais com nota fiscal."));
        executar(get("/vendedores/" + vendedor.getId()), null)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descricao").value("Eletrônicos originais com nota fiscal."))
                .andExpect(jsonPath("$.bannerUrl").value("https://loremflickr.com/1200/300/store"))
                .andExpect(jsonPath("$.lojaAberta").value(true));
        // A ordem do destaque depende das outras lojas do banco de teste; aqui só conferimos o formato e o limite.
        executar(get("/vendedores/destaque").param("limite", "3"), null)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", lessThanOrEqualTo(3)))
                .andExpect(jsonPath("$[*].lojaAberta", not(hasItem(false))));

        executar(patch("/vendedores/me/loja"), token, "{\"aberta\": false}")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lojaAberta").value(false));
        executar(get("/produtos/buscar").param("idVendedor", vendedor.getId().toString()), null)
                .andExpect(jsonPath("$.totalElements").value(0));
        executar(get("/produtos/" + produto.getId()), null).andExpect(status().isNotFound());
        executar(get("/produtos/" + produto.getId()), token).andExpect(status().isOk());
        executar(get("/vendedores/destaque").param("limite", "20"), null)
                .andExpect(jsonPath("$[*].id", not(hasItem(vendedor.getId().intValue()))));
        executar(post("/carrinhos/meu/itens"), login(criarCliente()), json("{'idProduto':%d,'quantidade':1}", produto.getId()))
                .andExpect(status().isUnprocessableEntity());

        executar(patch("/vendedores/me/loja"), token, "{\"aberta\": true}").andExpect(status().isOk());
        executar(get("/produtos/" + produto.getId()), null).andExpect(status().isOk());

        // Um vendedor não fecha nem edita a loja de outro.
        executar(patch("/vendedores/" + vendedor.getId()), login(criarVendedor()), "{\"descricao\":\"invadido\"}")
                .andExpect(status().isForbidden());
        executar(patch("/vendedores/me/loja"), login(criarCliente()), "{\"aberta\": false}").andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Cadastro de vendedor aceita descrição da loja")
    void cadastroComDescricao() throws Exception {
        int n = proximo();
        executar(post("/vendedores"), null, json("""
                {'nome':'Loja %d','razaoSocial':'Loja %d Ltda','cnpjCpf':'77.%03d.000/0001-00',
                 'email':'nova.loja%d@loja.com','senha':'senha123','descricao':'Tudo para pets.'}""", n, n, n % 1000, n))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.descricao").value("Tudo para pets."))
                .andExpect(jsonPath("$.lojaAberta").value(true));
    }

    // ---------------------------------------------------------------- Vitrine

    @Test
    @DisplayName("Busca filtra por avaliação mínima; mais vendidos é público e categorias aceitam size")
    void vitrineFiltrosEMaisVendidos() throws Exception {
        Vendedor vendedor = criarVendedor();
        Produto bemAvaliado = criarProduto(vendedor, "10.00", 5);
        bemAvaliado.setMediaAvaliacoes(4.5);
        produtoRepository.save(bemAvaliado);
        Produto malAvaliado = criarProduto(vendedor, "10.00", 5);
        malAvaliado.setMediaAvaliacoes(2.0);
        produtoRepository.save(malAvaliado);

        executar(get("/produtos/buscar").param("idVendedor", vendedor.getId().toString()).param("avaliacaoMin", "4"), null)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.content[0].id").value(bemAvaliado.getId()));
        executar(get("/produtos/buscar").param("avaliacaoMin", "7"), null).andExpect(status().isBadRequest());

        executar(get("/produtos/mais-vendidos").param("limite", "5"), null)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", lessThanOrEqualTo(5)))
                .andExpect(jsonPath("$.length()", greaterThan(0)));

        criarCategoria();
        executar(get("/categorias").param("size", "100"), null)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size").value(100));
    }

    // ---------------------------------------------------------------- Checkout: cupom

    @Test
    @DisplayName("Cliente confere o cupom antes de finalizar: válido 200; inexistente, inativo ou expirado 422")
    void validarCupom() throws Exception {
        Cupom valido = criarCupom("VALE" + proximo(), StatusCupom.ATIVO, LocalDate.now().plusDays(5));
        Cupom inativo = criarCupom("OFF" + proximo(), StatusCupom.INATIVO, null);
        Cupom expirado = criarCupom("OLD" + proximo(), StatusCupom.ATIVO, LocalDate.now().minusDays(1));
        String token = login(criarCliente());

        executar(get("/cupons/validar").param("codigo", valido.getCodigo().toLowerCase()), token)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codigo").value(valido.getCodigo()))
                .andExpect(jsonPath("$.valorDesconto").value(10.0));
        executar(get("/cupons/validar").param("codigo", "NAOEXISTE"), token)
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.detail", containsString("não existe")));
        executar(get("/cupons/validar").param("codigo", inativo.getCodigo()), token).andExpect(status().isUnprocessableEntity());
        executar(get("/cupons/validar").param("codigo", expirado.getCodigo()), token).andExpect(status().isUnprocessableEntity());
        executar(get("/cupons/validar").param("codigo", valido.getCodigo()), null).andExpect(status().isUnauthorized());
        // A listagem de cupons continua só para o ADMIN.
        executar(get("/cupons"), token).andExpect(status().isForbidden());
    }

    // ---------------------------------------------------------------- Avaliações

    @Test
    @DisplayName("Resumo de avaliações é público; pode-avaliar explica o motivo; vendedor vê as recebidas")
    void avaliacoesResumoPodeAvaliarERecebidas() throws Exception {
        Vendedor vendedor = criarVendedor();
        Produto produto = criarProduto(vendedor, "30.00", 5);
        avaliar(criarCliente(), produto, 5);
        avaliar(criarCliente(), produto, 5);
        avaliar(criarCliente(), produto, 2);

        executar(get("/avaliacoes/resumo").param("idProduto", produto.getId().toString()), null)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(3))
                .andExpect(jsonPath("$.media").value(4.0))
                .andExpect(jsonPath("$.distribuicao.5").value(2))
                .andExpect(jsonPath("$.distribuicao.2").value(1))
                .andExpect(jsonPath("$.distribuicao.1").value(0));

        executar(get("/avaliacoes/pode-avaliar").param("idProduto", produto.getId().toString()), login(criarCliente()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.podeAvaliar").value(false))
                .andExpect(jsonPath("$.motivo", containsString("recebeu")));

        executar(get("/avaliacoes/recebidas"), login(vendedor))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(3));
        executar(get("/avaliacoes/recebidas"), login(criarVendedor()))
                .andExpect(jsonPath("$.totalElements").value(0));
        executar(get("/avaliacoes/recebidas"), login(criarCliente())).andExpect(status().isForbidden());
    }

    // ---------------------------------------------------------------- Devoluções

    @Test
    @DisplayName("Devoluções recebidas: vendedor e admin podem listar; cliente não")
    void devolucoesRecebidas() throws Exception {
        executar(get("/devolucoes/recebidas"), login(criarVendedor()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(0));
        executar(get("/devolucoes/recebidas").param("status", "PENDENTE"), login(criarAdmin())).andExpect(status().isOk());
        executar(get("/devolucoes/recebidas"), login(criarCliente())).andExpect(status().isForbidden());
    }

    // ---------------------------------------------------------------- Favoritos

    @Test
    @DisplayName("Favoritar e desfavoritar são idempotentes; só cliente tem lista de desejos")
    void favoritos() throws Exception {
        Produto produto = criarProduto(criarVendedor(), "40.00", 5);
        String token = login(criarCliente());

        executar(put("/favoritos/" + produto.getId()), token).andExpect(status().isNoContent());
        executar(put("/favoritos/" + produto.getId()), token).andExpect(status().isNoContent());
        executar(get("/favoritos/ids"), token)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0]").value(produto.getId()));
        executar(get("/favoritos"), token)
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.content[0].nome").value(produto.getNome()));

        executar(delete("/favoritos/" + produto.getId()), token).andExpect(status().isNoContent());
        executar(delete("/favoritos/" + produto.getId()), token).andExpect(status().isNoContent());
        executar(get("/favoritos/ids"), token).andExpect(jsonPath("$.length()").value(0));

        Produto pausado = criarProduto(criarVendedor(), "40.00", 5, false);
        executar(put("/favoritos/" + pausado.getId()), token).andExpect(status().isNotFound());
        executar(get("/favoritos"), login(criarVendedor())).andExpect(status().isForbidden());
        executar(get("/favoritos"), null).andExpect(status().isUnauthorized());
    }

    // ---------------------------------------------------------------- Carrinho de visitante

    @Test
    @DisplayName("Mesclar carrinho de visitante soma com o carrinho, limita ao estoque e ignora indisponíveis")
    void mesclarCarrinho() throws Exception {
        Cliente cliente = criarCliente();
        Produto comEstoque = criarProduto(criarVendedor(), "10.00", 3);
        Produto outro = criarProduto(criarVendedor(), "20.00", 10);
        Produto pausado = criarProduto(criarVendedor(), "20.00", 10, false);
        String token = login(cliente);
        executar(post("/carrinhos/meu/itens"), token, json("{'idProduto':%d,'quantidade':2}", comEstoque.getId()))
                .andExpect(status().isOk());

        executar(post("/carrinhos/meu/mesclar"), token, json(
                "{'itens':[{'idProduto':%d,'quantidade':5},{'idProduto':%d,'quantidade':1},{'idProduto':%d,'quantidade':1},{'idProduto':999999,'quantidade':1}]}",
                comEstoque.getId(), outro.getId(), pausado.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itens.length()").value(2))
                .andExpect(jsonPath("$.itens[?(@.idProduto == %d)].quantidade".formatted(comEstoque.getId())).value(3))
                .andExpect(jsonPath("$.itens[?(@.idProduto == %d)].quantidade".formatted(outro.getId())).value(1));

        executar(post("/carrinhos/meu/mesclar"), token, "{\"itens\": [{\"idProduto\": 1, \"quantidade\": 0}]}")
                .andExpect(status().isBadRequest());
    }

    // ---------------------------------------------------------------- Notificações

    @Test
    @DisplayName("Checkout avisa o vendedor; o cliente é avisado quando o vendedor avança o pedido")
    void notificacoesDoPedido() throws Exception {
        Cliente cliente = criarCliente();
        Endereco endereco = criarEndereco(cliente);
        Vendedor vendedor = criarVendedor();
        Produto produto = criarProduto(vendedor, "60.00", 5);
        String tokenCliente = login(cliente);
        String tokenVendedor = login(vendedor);

        Integer idPedido = comprar(tokenCliente, produto, endereco, "PIX");

        executar(get("/notificacoes/nao-lidas/contagem"), tokenVendedor)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.naoLidas").value(1));
        Integer idNotificacao = ler(executar(get("/notificacoes"), tokenVendedor)
                .andExpect(jsonPath("$.content[0].titulo", containsString("Novo pedido")))
                .andExpect(jsonPath("$.content[0].idPedido").value(idPedido))
                .andExpect(jsonPath("$.content[0].link").value("/vendedor/pedidos/" + idPedido)), "$.content[0].id");
        // O cliente fez a compra, então não recebe aviso da própria ação.
        executar(get("/notificacoes/nao-lidas/contagem"), tokenCliente).andExpect(jsonPath("$.naoLidas").value(0));

        executar(patch("/pedidos/" + idPedido + "/status"), tokenVendedor, "{\"status\":\"PAGAMENTO_APROVADO\"}")
                .andExpect(status().isOk());
        executar(get("/notificacoes").param("somenteNaoLidas", "true"), tokenCliente)
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.content[0].titulo", containsString("Pagamento aprovado")))
                .andExpect(jsonPath("$.content[0].link").value("/conta/pedidos/" + idPedido));

        // Ninguém marca como lida a notificação de outro usuário.
        executar(patch("/notificacoes/" + idNotificacao + "/lida"), tokenCliente).andExpect(status().isNotFound());
        executar(patch("/notificacoes/" + idNotificacao + "/lida"), tokenVendedor)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lida").value(true));
        executar(patch("/notificacoes/lidas"), tokenCliente).andExpect(jsonPath("$.naoLidas").value(0));
        executar(get("/notificacoes/nao-lidas/contagem"), tokenCliente).andExpect(jsonPath("$.naoLidas").value(0));
    }

    // ---------------------------------------------------------------- Exclusão de conta

    @Test
    @DisplayName("Cliente sem pedidos exclui a conta: é apagado de vez e não consegue mais logar")
    void clienteSemPedidosExcluiConta() throws Exception {
        Cliente cliente = criarCliente();
        criarEndereco(cliente);
        Produto produto = criarProduto(criarVendedor(), "10.00", 5);
        String token = login(cliente);
        executar(put("/favoritos/" + produto.getId()), token).andExpect(status().isNoContent());
        executar(post("/carrinhos/meu/itens"), token, json("{'idProduto':%d,'quantidade':1}", produto.getId()))
                .andExpect(status().isOk());

        executar(delete("/usuarios/me"), token, "{\"senha\":\"errada\"}").andExpect(status().isBadRequest());
        executar(delete("/usuarios/me"), token, json("{'senha':'%s'}", SENHA)).andExpect(status().isNoContent());

        assertThat(clienteRepository.findById(cliente.getId())).isEmpty();
        loginFalha(cliente.getEmail());
        executar(get("/auth/me"), token).andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Cliente com pedidos exclui a conta: é anonimizado e os pedidos continuam no histórico")
    void clienteComPedidosExcluiConta() throws Exception {
        Cliente cliente = criarCliente();
        Endereco endereco = criarEndereco(cliente);
        Produto produto = criarProduto(criarVendedor(), "10.00", 5);
        String token = login(cliente);
        Integer idPedido = comprar(token, produto, endereco, "CARTAO");

        executar(delete("/usuarios/me"), token, json("{'senha':'%s'}", SENHA)).andExpect(status().isNoContent());

        Cliente anonimizado = clienteRepository.findById(cliente.getId()).orElseThrow();
        assertThat(anonimizado.isExcluido()).isTrue();
        assertThat(anonimizado.isAtivo()).isFalse();
        assertThat(anonimizado.getNome()).isEqualTo("Usuário removido");
        assertThat(anonimizado.getEmail()).isNotEqualTo(cliente.getEmail());
        assertThat(pedidoRepository.findById(idPedido.longValue())).isPresent();
        loginFalha(cliente.getEmail());

        // O e-mail fica livre para um novo cadastro e a conta não aparece mais para o admin.
        executar(post("/clientes"), null, json("{'nome':'Novo','email':'%s','senha':'senha123'}", cliente.getEmail()))
                .andExpect(status().isCreated());
        executar(patch("/usuarios/" + cliente.getId() + "/ativo"), login(criarAdmin()), "{\"ativo\": true}")
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @DisplayName("Vendedor sem vendas exclui a conta e os produtos vão junto (inclusive de carrinhos e favoritos)")
    void vendedorSemVendasExcluiContaComProdutos() throws Exception {
        Vendedor vendedor = criarVendedor();
        Produto produtoA = criarProduto(vendedor, "10.00", 5);
        Produto produtoB = criarProduto(vendedor, "20.00", 5);
        String tokenCliente = login(criarCliente());
        executar(put("/favoritos/" + produtoA.getId()), tokenCliente).andExpect(status().isNoContent());
        executar(post("/carrinhos/meu/itens"), tokenCliente, json("{'idProduto':%d,'quantidade':1}", produtoB.getId()))
                .andExpect(status().isOk());

        executar(delete("/usuarios/me"), login(vendedor), json("{'senha':'%s'}", SENHA)).andExpect(status().isNoContent());

        assertThat(vendedorRepository.findById(vendedor.getId())).isEmpty();
        assertThat(produtoRepository.findById(produtoA.getId())).isEmpty();
        assertThat(produtoRepository.findById(produtoB.getId())).isEmpty();
        executar(get("/favoritos/ids"), tokenCliente).andExpect(jsonPath("$.length()").value(0));
        executar(get("/carrinhos/meu"), tokenCliente).andExpect(jsonPath("$.itens.length()").value(0));
    }

    @Test
    @DisplayName("Vendedor com vendas: bloqueado se há pedido em andamento; depois, anonimizado e só os produtos não vendidos são apagados")
    void vendedorComVendasExcluiConta() throws Exception {
        Cliente cliente = criarCliente();
        Endereco endereco = criarEndereco(cliente);
        Vendedor vendedor = criarVendedor();
        Produto vendido = criarProduto(vendedor, "10.00", 5);
        Produto naoVendido = criarProduto(vendedor, "20.00", 5);
        String tokenCliente = login(cliente);
        String tokenVendedor = login(vendedor);
        Integer idPedido = comprar(tokenCliente, vendido, endereco, "PIX");

        executar(delete("/usuarios/me"), tokenVendedor, json("{'senha':'%s'}", SENHA))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.detail", containsString("pedidos em andamento")));

        executar(patch("/pedidos/" + idPedido + "/cancelar"), tokenCliente).andExpect(status().isOk());
        executar(delete("/usuarios/me"), tokenVendedor, json("{'senha':'%s'}", SENHA)).andExpect(status().isNoContent());

        Vendedor anonimizado = vendedorRepository.findById(vendedor.getId()).orElseThrow();
        assertThat(anonimizado.isExcluido()).isTrue();
        assertThat(anonimizado.isLojaAberta()).isFalse();
        assertThat(produtoRepository.findById(naoVendido.getId())).isEmpty();
        assertThat(produtoRepository.findById(vendido.getId())).get().matches(p -> !p.isAtivo());
        executar(get("/vendedores/" + vendedor.getId()), null).andExpect(status().isNotFound());
        loginFalha(vendedor.getEmail());
    }

    @Test
    @DisplayName("Admin pode excluir a própria conta, menos o admin padrão; admin exclui cliente com as mesmas regras")
    void exclusaoPeloAdmin() throws Exception {
        Admin admin = criarAdmin();
        executar(delete("/usuarios/me"), login(admin), json("{'senha':'%s'}", SENHA)).andExpect(status().isNoContent());
        assertThat(adminRepository.findById(admin.getId())).isEmpty();

        Usuario padrao = usuarioRepository.findByEmail("admin@marketplace.com").orElseThrow();
        String tokenPadrao = mvcLogin("admin@marketplace.com", "admin123");
        executar(delete("/usuarios/me"), tokenPadrao, "{\"senha\":\"admin123\"}")
                .andExpect(status().isUnprocessableEntity());
        assertThat(usuarioRepository.findById(padrao.getId())).isPresent();

        Cliente cliente = criarCliente();
        executar(delete("/clientes/" + cliente.getId()), login(criarAdmin())).andExpect(status().isNoContent());
        assertThat(clienteRepository.findById(cliente.getId())).isEmpty();
    }

    // ---------------------------------------------------------------- auxiliares

    private Integer comprar(String tokenCliente, Produto produto, Endereco endereco, String metodo) throws Exception {
        executar(post("/carrinhos/meu/itens"), tokenCliente, json("{'idProduto':%d,'quantidade':1}", produto.getId()))
                .andExpect(status().isOk());
        return ler(executar(post("/checkout"), tokenCliente,
                json("{'idEndereco':%d,'metodoPagamento':'%s'}", endereco.getId(), metodo))
                .andExpect(status().isCreated()), "$.pedidos[0].id");
    }

    private Cupom criarCupom(String codigo, StatusCupom status, LocalDate validade) {
        Cupom cupom = new Cupom();
        cupom.setCodigo(codigo);
        cupom.setValorDesconto(new BigDecimal("10.00"));
        cupom.setStatus(status);
        cupom.setDataExpiracao(validade);
        return cupomRepository.save(cupom);
    }

    private void avaliar(Cliente cliente, Produto produto, int nota) {
        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setCliente(cliente);
        avaliacao.setProduto(produto);
        avaliacao.setNota(nota);
        avaliacao.setDataAvaliacao(LocalDateTime.now());
        avaliacaoRepository.save(avaliacao);
    }

    private String mvcLogin(String email, String senha) throws Exception {
        return ler(mvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
                .content(json("{'email':'%s','senha':'%s'}", email, senha))).andExpect(status().isOk()), "$.token");
    }

    private void loginFalha(String email) throws Exception {
        mvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content(json("{'email':'%s','senha':'%s'}", email, SENHA)))
                .andExpect(result -> assertThat(result.getResponse().getStatus()).isIn(401, 403));
    }
}
