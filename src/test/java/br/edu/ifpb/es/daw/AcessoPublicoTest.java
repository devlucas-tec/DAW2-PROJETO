package br.edu.ifpb.es.daw;

import br.edu.ifpb.es.daw.model.Admin;
import br.edu.ifpb.es.daw.model.Cliente;
import br.edu.ifpb.es.daw.model.Produto;
import br.edu.ifpb.es.daw.model.Vendedor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Endpoints públicos x protegidos, /auth/me e vitrine")
class AcessoPublicoTest extends IntegracaoTestBase {

    @Test
    @DisplayName("GETs do catálogo respondem 200 sem token")
    void catalogoPublicoSemToken() throws Exception {
        Vendedor vendedor = criarVendedor();
        Produto produto = criarProduto(vendedor, "50.00", 10);

        executar(get("/produtos"), null).andExpect(status().isOk());
        executar(get("/produtos/" + produto.getId()), null).andExpect(status().isOk());
        executar(get("/produtos/buscar").param("nome", "Produto"), null).andExpect(status().isOk());
        executar(get("/categorias"), null).andExpect(status().isOk());
        executar(get("/categorias/" + produto.getCategoria().getId()), null).andExpect(status().isOk());
        executar(get("/avaliacoes").param("idProduto", produto.getId().toString()), null).andExpect(status().isOk());
        executar(get("/vendedores/" + vendedor.getId()), null)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.razaoSocial").value(vendedor.getRazaoSocial()))
                .andExpect(jsonPath("$.email").doesNotExist())
                .andExpect(jsonPath("$.cnpjCpf").doesNotExist());
    }

    @Test
    @DisplayName("Escritas e listagens sensíveis continuam exigindo token e perfil")
    void escritasExigemTokenEPerfil() throws Exception {
        Vendedor vendedor = criarVendedor();
        Produto produto = criarProduto(vendedor, "50.00", 10);
        Cliente cliente = criarCliente();
        Admin admin = criarAdmin();

        executar(post("/produtos"), null, "{}").andExpect(status().isUnauthorized());
        executar(patch("/produtos/" + produto.getId()), null, "{}").andExpect(status().isUnauthorized());
        executar(delete("/produtos/" + produto.getId()), null).andExpect(status().isUnauthorized());
        executar(post("/categorias"), null, "{}").andExpect(status().isUnauthorized());
        executar(get("/avaliacoes"), null).andExpect(status().isUnauthorized());

        executar(get("/vendedores"), null).andExpect(status().isUnauthorized());
        executar(get("/vendedores"), login(cliente)).andExpect(status().isForbidden());
        executar(get("/vendedores"), login(admin))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].email").exists());
    }

    @Test
    @DisplayName("GET /auth/me devolve o usuário do token e o login traz id e nome")
    void authMe() throws Exception {
        Cliente cliente = criarCliente();

        executar(get("/auth/me"), null).andExpect(status().isUnauthorized());

        executar(post("/auth/login"), null, json("{'email':'%s','senha':'%s'}", cliente.getEmail(), SENHA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(cliente.getId()))
                .andExpect(jsonPath("$.nome").value(cliente.getNome()))
                .andExpect(jsonPath("$.role").value("CLIENTE"));

        executar(get("/auth/me"), login(cliente))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(cliente.getId()))
                .andExpect(jsonPath("$.nome").value(cliente.getNome()))
                .andExpect(jsonPath("$.email").value(cliente.getEmail()))
                .andExpect(jsonPath("$.role").value("CLIENTE"));
    }

    @Test
    @DisplayName("Vitrine esconde produtos inativos, sem estoque e de vendedor bloqueado")
    void vitrineEscondeIndisponiveis() throws Exception {
        Vendedor vendedor = criarVendedor();
        Produto ativo = criarProduto(vendedor, "10.00", 5);
        Produto inativo = criarProduto(vendedor, "20.00", 5, false);
        Produto semEstoque = criarProduto(vendedor, "30.00", 0);

        executar(get("/produtos/buscar").param("idVendedor", vendedor.getId().toString()), null)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.content[0].id").value(ativo.getId()));

        executar(get("/produtos/buscar").param("idVendedor", vendedor.getId().toString())
                .param("somenteComEstoque", "false"), null)
                .andExpect(jsonPath("$.content[*].id", containsInAnyOrder(
                        ativo.getId().intValue(), semEstoque.getId().intValue())));

        // Anúncio pausado: 404 para o público, visível para o dono.
        executar(get("/produtos/" + inativo.getId()), null).andExpect(status().isNotFound());
        executar(get("/produtos/" + inativo.getId()), login(vendedor)).andExpect(status().isOk());

        // ADMIN bloqueia o vendedor: os produtos somem e ele não consegue mais logar.
        Admin admin = criarAdmin();
        executar(patch("/usuarios/" + vendedor.getId() + "/ativo"), login(admin), "{\"ativo\":false}")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ativo").value(false));
        executar(get("/produtos/buscar").param("idVendedor", vendedor.getId().toString()), null)
                .andExpect(jsonPath("$.totalElements").value(0));
        executar(post("/auth/login"), null, json("{'email':'%s','senha':'%s'}", vendedor.getEmail(), SENHA))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Ordenação vale para o conjunto todo, não só para a página")
    void ordenacaoEntrePaginas() throws Exception {
        Vendedor vendedor = criarVendedor();
        criarProduto(vendedor, "30.00", 5);
        criarProduto(vendedor, "10.00", 5);
        criarProduto(vendedor, "20.00", 5);

        executar(get("/produtos/buscar").param("idVendedor", vendedor.getId().toString())
                .param("sort", "preco,asc").param("size", "2"), null)
                .andExpect(jsonPath("$.content[*].preco", contains(10.0, 20.0)))
                .andExpect(jsonPath("$.totalPages").value(2));
        executar(get("/produtos/buscar").param("idVendedor", vendedor.getId().toString())
                .param("sort", "preco,asc").param("size", "2").param("page", "1"), null)
                .andExpect(jsonPath("$.content[*].preco", contains(30.0)));
        executar(get("/produtos/buscar").param("idVendedor", vendedor.getId().toString())
                .param("sort", "preco,desc").param("size", "2"), null)
                .andExpect(jsonPath("$.content[*].preco", contains(30.0, 20.0)));

        executar(get("/produtos/buscar").param("sort", "estoque,asc"), null).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Produto sem avaliações tem média null e total 0")
    void produtoSemAvaliacoes() throws Exception {
        Produto produto = criarProduto(criarVendedor(), "10.00", 5);
        executar(get("/produtos/" + produto.getId()), null)
                .andExpect(jsonPath("$.mediaAvaliacoes").value(nullValue()))
                .andExpect(jsonPath("$.totalAvaliacoes").value(0));
    }
}
