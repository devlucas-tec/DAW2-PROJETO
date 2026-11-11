package br.edu.ifpb.es.daw;

import br.edu.ifpb.es.daw.model.Cliente;
import br.edu.ifpb.es.daw.model.Endereco;
import br.edu.ifpb.es.daw.model.Produto;
import br.edu.ifpb.es.daw.model.Vendedor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Endpoints dos painéis do cliente, do vendedor e do admin")
class PaineisTest extends IntegracaoTestBase {

    @Test
    @DisplayName("Cliente troca a senha conferindo a atual e lista as próprias devoluções")
    void painelCliente() throws Exception {
        Cliente cliente = criarCliente();
        String token = login(cliente);

        executar(patch("/clientes/me/senha"), token, "{\"senhaAtual\":\"errada\",\"novaSenha\":\"novaSenha1\"}")
                .andExpect(status().isBadRequest());
        executar(patch("/clientes/me/senha"), token, json("{'senhaAtual':'%s','novaSenha':'novaSenha1'}", SENHA))
                .andExpect(status().isNoContent());
        executar(post("/auth/login"), null, json("{'email':'%s','senha':'novaSenha1'}", cliente.getEmail()))
                .andExpect(status().isOk());

        executar(get("/devolucoes/minhas"), token).andExpect(status().isOk()).andExpect(jsonPath("$.totalElements").value(0));

        // GET /enderecos só traz os endereços do cliente do token.
        criarEndereco(cliente);
        criarEndereco(criarCliente());
        executar(get("/enderecos"), token).andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    @DisplayName("Dashboard do vendedor considera só os itens dele")
    void dashboardVendedor() throws Exception {
        Cliente cliente = criarCliente();
        Endereco endereco = criarEndereco(cliente);
        Vendedor vendedorA = criarVendedor();
        Vendedor vendedorB = criarVendedor();
        Produto produtoA = criarProduto(vendedorA, "100.00", 7);
        Produto produtoB = criarProduto(vendedorB, "40.00", 10);
        String tokenCliente = login(cliente);

        executar(post("/carrinhos/meu/itens"), tokenCliente, json("{'idProduto':%d,'quantidade':3}", produtoA.getId()));
        executar(post("/carrinhos/meu/itens"), tokenCliente, json("{'idProduto':%d,'quantidade':1}", produtoB.getId()));
        executar(post("/checkout"), tokenCliente, json("{'idEndereco':%d,'metodoPagamento':'CARTAO'}", endereco.getId()))
                .andExpect(status().isCreated());

        executar(get("/dashboard/vendedor").param("periodo", "7d"), login(vendedorA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.faturamento").value(300.0))
                .andExpect(jsonPath("$.totalPedidos").value(1))
                .andExpect(jsonPath("$.ticketMedio").value(300.0))
                .andExpect(jsonPath("$.pedidosPorStatus.CONFIRMADO").value(1))
                .andExpect(jsonPath("$.serie.length()").value(7))
                .andExpect(jsonPath("$.topProdutos[0].idProduto").value(produtoA.getId()))
                .andExpect(jsonPath("$.topProdutos[0].quantidadeVendida").value(3))
                .andExpect(jsonPath("$.estoqueBaixo[0].idProduto").value(produtoA.getId()))
                .andExpect(jsonPath("$.mediaAvaliacoes").doesNotExist());

        executar(get("/dashboard/vendedor").param("periodo", "1ano"), login(vendedorA)).andExpect(status().isBadRequest());
        executar(get("/dashboard/vendedor"), tokenCliente).andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Dashboard do admin e listagem de usuários")
    void dashboardAdmin() throws Exception {
        String tokenAdmin = login(criarAdmin());

        executar(get("/dashboard/admin").param("periodo", "12m"), tokenAdmin)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.serie.length()").value(12))
                .andExpect(jsonPath("$.novosUsuarios.total").exists())
                .andExpect(jsonPath("$.gmv").exists());
        executar(get("/usuarios").param("role", "ADMIN"), tokenAdmin).andExpect(status().isOk());
        executar(get("/dashboard/admin"), login(criarVendedor())).andExpect(status().isForbidden());
    }
}
