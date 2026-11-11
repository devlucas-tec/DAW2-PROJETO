package br.edu.ifpb.es.daw;

import br.edu.ifpb.es.daw.model.Cliente;
import br.edu.ifpb.es.daw.model.Endereco;
import br.edu.ifpb.es.daw.model.Produto;
import br.edu.ifpb.es.daw.model.Vendedor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Ciclo de vida do pedido e regra de avaliação após a entrega")
class CicloPedidoTest extends IntegracaoTestBase {

    @Test
    @DisplayName("Vendedor avança o pedido etapa por etapa; transições inválidas dão 422; avaliação só após ENTREGUE")
    void cicloCompleto() throws Exception {
        Cliente cliente = criarCliente();
        Endereco endereco = criarEndereco(cliente);
        Vendedor vendedor = criarVendedor();
        Produto produto = criarProduto(vendedor, "80.00", 10);
        String tokenCliente = login(cliente);
        String tokenVendedor = login(vendedor);
        String tokenOutroVendedor = login(criarVendedor());

        executar(post("/carrinhos/meu/itens"), tokenCliente, json("{'idProduto':%d,'quantidade':1}", produto.getId()));
        Integer idPedido = ler(executar(post("/checkout"), tokenCliente,
                json("{'idEndereco':%d,'metodoPagamento':'PIX'}", endereco.getId())), "$.pedidos[0].id");
        String urlStatus = "/pedidos/" + idPedido + "/status";
        String avaliacao = json("{'idProduto':%d,'nota':5,'comentario':'Muito bom'}", produto.getId());

        executar(get("/pedidos/" + idPedido), tokenCliente)
                .andExpect(jsonPath("$.etapaAtual").value("PEDIDO_CONFIRMADO"))
                .andExpect(jsonPath("$.statusPagamento").value("PENDENTE"))
                .andExpect(jsonPath("$.proximaEtapa").value("PAGAMENTO_APROVADO"));

        // Antes da entrega o cliente não pode avaliar.
        executar(post("/avaliacoes"), tokenCliente, avaliacao).andExpect(status().isUnprocessableEntity());

        // Outro vendedor e o próprio cliente não mudam o status.
        executar(patch(urlStatus), tokenOutroVendedor, "{\"status\":\"PAGAMENTO_APROVADO\"}").andExpect(status().isForbidden());
        executar(patch(urlStatus), tokenCliente, "{\"status\":\"PAGAMENTO_APROVADO\"}").andExpect(status().isForbidden());

        // Não dá para embalar com o pagamento pendente.
        executar(patch(urlStatus), tokenVendedor, "{\"status\":\"EMBALADO\"}").andExpect(status().isUnprocessableEntity());

        executar(patch(urlStatus), tokenVendedor, "{\"status\":\"PAGAMENTO_APROVADO\"}")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.etapaAtual").value("PAGAMENTO_APROVADO"));
        executar(patch(urlStatus), tokenVendedor, "{\"status\":\"EMBALADO\"}")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.etapaAtual").value("EMBALADO"));

        // EMBALADO → ENTREGUE pula etapas.
        executar(patch(urlStatus), tokenVendedor, "{\"status\":\"ENTREGUE\"}")
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.detail", containsString("EM_TRANSITO")));

        // EM_TRANSITO exige transportadora e rastreio.
        executar(patch(urlStatus), tokenVendedor, "{\"status\":\"EM_TRANSITO\"}").andExpect(status().isUnprocessableEntity());
        String rastreio = "BRTESTE" + proximo();
        executar(patch(urlStatus), tokenVendedor,
                json("{'status':'EM_TRANSITO','transportadora':'Correios','codigoRastreamento':'%s'}", rastreio))
                .andExpect(status().isOk());

        // O cliente vê a atualização imediatamente.
        executar(get("/pedidos/" + idPedido), tokenCliente)
                .andExpect(jsonPath("$.etapaAtual").value("EM_TRANSITO"))
                .andExpect(jsonPath("$.codigoRastreamento").value(rastreio))
                .andExpect(jsonPath("$.transportadora").value("Correios"));

        executar(patch(urlStatus), tokenVendedor, "{\"status\":\"SAIU_PARA_ENTREGA\"}").andExpect(status().isOk());
        executar(patch(urlStatus), tokenVendedor, "{\"status\":\"ENTREGUE\"}")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.etapaAtual").value("ENTREGUE"))
                .andExpect(jsonPath("$.proximaEtapa").doesNotExist());
        executar(patch(urlStatus), tokenVendedor, "{\"status\":\"ENTREGUE\"}").andExpect(status().isUnprocessableEntity());

        executar(get("/pedidos/" + idPedido + "/historico"), tokenCliente)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].etapa", contains("PEDIDO_CONFIRMADO", "PAGAMENTO_APROVADO", "EMBALADO",
                        "EM_TRANSITO", "SAIU_PARA_ENTREGA", "ENTREGUE")))
                .andExpect(jsonPath("$[1].nomeAutor").value(vendedor.getNome()));

        // Entregue: agora pode avaliar, e a média do produto é atualizada.
        executar(post("/avaliacoes"), tokenCliente, avaliacao).andExpect(status().isCreated());
        executar(get("/produtos/" + produto.getId()), null)
                .andExpect(jsonPath("$.mediaAvaliacoes").value(5.0))
                .andExpect(jsonPath("$.totalAvaliacoes").value(1));
        executar(get("/avaliacoes").param("idProduto", produto.getId().toString()), null)
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    @DisplayName("Pagamento com cartão já começa aprovado; filtro de pedidos por status funciona")
    void cartaoAprovadoEFiltroPorStatus() throws Exception {
        Cliente cliente = criarCliente();
        Endereco endereco = criarEndereco(cliente);
        Produto produto = criarProduto(criarVendedor(), "15.00", 10);
        String token = login(cliente);

        executar(post("/carrinhos/meu/itens"), token, json("{'idProduto':%d,'quantidade':1}", produto.getId()));
        Integer idPedido = ler(executar(post("/checkout"), token,
                json("{'idEndereco':%d,'metodoPagamento':'CARTAO'}", endereco.getId())), "$.pedidos[0].id");

        executar(get("/pedidos/" + idPedido), token)
                .andExpect(jsonPath("$.statusPagamento").value("APROVADO"))
                .andExpect(jsonPath("$.proximaEtapa").value("EMBALADO"));

        executar(get("/pedidos").param("status", "CONFIRMADO"), token).andExpect(jsonPath("$.totalElements").value(1));
        executar(get("/pedidos").param("status", "CANCELADO"), token).andExpect(jsonPath("$.totalElements").value(0));

        executar(patch("/pedidos/" + idPedido + "/cancelar"), token)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusPagamento").value("ESTORNADO"));
        executar(get("/pedidos").param("status", "CANCELADO"), token).andExpect(jsonPath("$.totalElements").value(1));
    }
}
