package br.edu.ifpb.es.daw;

import br.edu.ifpb.es.daw.model.Admin;
import br.edu.ifpb.es.daw.model.Produto;
import br.edu.ifpb.es.daw.model.Vendedor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Propriedade de produto: vendedor A x vendedor B")
class PropriedadeProdutoTest extends IntegracaoTestBase {

    @Test
    @DisplayName("Vendedor A não altera nem exclui produto do vendedor B; ADMIN altera qualquer um")
    void vendedorNaoMexeEmProdutoDeOutro() throws Exception {
        Vendedor vendedorA = criarVendedor();
        Vendedor vendedorB = criarVendedor();
        Produto produtoB = criarProduto(vendedorB, "100.00", 10);
        String tokenA = login(vendedorA);

        executar(patch("/produtos/" + produtoB.getId()), tokenA, "{\"preco\": 1.00}").andExpect(status().isForbidden());
        executar(patch("/produtos/" + produtoB.getId()), tokenA, "{\"ativo\": false}").andExpect(status().isForbidden());
        executar(delete("/produtos/" + produtoB.getId()), tokenA).andExpect(status().isForbidden());

        Admin admin = criarAdmin();
        executar(patch("/produtos/" + produtoB.getId()), login(admin), "{\"preco\": 90.00}")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.preco").value(90.0));
    }

    @Test
    @DisplayName("O idVendedor enviado no corpo por um VENDEDOR é ignorado")
    void idVendedorDoCorpoIgnorado() throws Exception {
        Vendedor vendedorA = criarVendedor();
        Vendedor vendedorB = criarVendedor();
        Long idCategoria = criarCategoria().getId();

        executar(post("/produtos"), login(vendedorA), json("""
                {'nome':'Produto do A','preco':10.5,'estoque':3,'idCategoria':%d,'idVendedor':%d,
                 'imagemUrl':'https://loremflickr.com/640/640/phone?lock=9'}""", idCategoria, vendedorB.getId()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idVendedor").value(vendedorA.getId()))
                .andExpect(jsonPath("$.ativo").value(true));
    }

    @Test
    @DisplayName("imagemUrl é obrigatória e precisa ser uma URL http(s)")
    void imagemUrlValidada() throws Exception {
        Vendedor vendedor = criarVendedor();
        Long idCategoria = criarCategoria().getId();
        String token = login(vendedor);

        executar(post("/produtos"), token, json("{'nome':'Sem imagem','preco':10,'estoque':1,'idCategoria':%d}", idCategoria))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erros.imagemUrl").exists());
        executar(post("/produtos"), token, json("{'nome':'Imagem ruim','preco':10,'estoque':1,'idCategoria':%d,'imagemUrl':'nao-e-url'}", idCategoria))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /produtos/meus lista só os produtos do vendedor, inclusive inativos e sem estoque")
    void meusProdutos() throws Exception {
        Vendedor vendedorA = criarVendedor();
        Vendedor vendedorB = criarVendedor();
        Produto ativo = criarProduto(vendedorA, "10.00", 5);
        Produto inativo = criarProduto(vendedorA, "10.00", 5, false);
        Produto semEstoque = criarProduto(vendedorA, "10.00", 0);
        criarProduto(vendedorB, "10.00", 5);
        String tokenA = login(vendedorA);

        executar(get("/produtos/meus"), tokenA)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[*].id", containsInAnyOrder(
                        ativo.getId().intValue(), inativo.getId().intValue(), semEstoque.getId().intValue())));
        executar(get("/produtos/meus").param("ativo", "false"), tokenA)
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.content[0].id").value(inativo.getId()));

        executar(get("/produtos/meus"), null).andExpect(status().isUnauthorized());
        executar(get("/produtos/meus"), login(criarCliente())).andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Pausar o anúncio pelo PATCH tira o produto da vitrine")
    void pausarAnuncio() throws Exception {
        Vendedor vendedor = criarVendedor();
        Produto produto = criarProduto(vendedor, "10.00", 5);

        executar(patch("/produtos/" + produto.getId()), login(vendedor), "{\"ativo\": false}")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ativo").value(false))
                .andExpect(jsonPath("$.preco").value(10.0));
        executar(get("/produtos/buscar").param("idVendedor", vendedor.getId().toString()), null)
                .andExpect(jsonPath("$.totalElements").value(0));
    }
}
