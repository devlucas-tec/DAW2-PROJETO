package br.edu.ifpb.es.daw;

import br.edu.ifpb.es.daw.model.Cliente;
import br.edu.ifpb.es.daw.model.Endereco;
import br.edu.ifpb.es.daw.model.Produto;
import br.edu.ifpb.es.daw.model.Vendedor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Carrinho: nenhum fluxo pode gerar INSERT duplicado em item_carrinho")
class CarrinhoDuplicadoTest extends IntegracaoTestBase {

    @Test
    @DisplayName("Mesclar com produto que já está no carrinho e depois finalizar")
    void mesclarComItemExistenteEFinalizar() throws Exception {
        Cliente cliente = criarCliente();
        Endereco endereco = criarEndereco(cliente);
        Vendedor vendedor = criarVendedor();
        Produto p1 = criarProduto(vendedor, "10.00", 10);
        Produto p2 = criarProduto(vendedor, "20.00", 10);
        String token = login(cliente);

        executar(post("/carrinhos/meu/itens"), token, json("{'idProduto':%d,'quantidade':1}", p1.getId()))
                .andExpect(status().isOk());
        executar(post("/carrinhos/meu/mesclar"), token, json(
                "{'itens':[{'idProduto':%d,'quantidade':2},{'idProduto':%d,'quantidade':1}]}", p1.getId(), p2.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itens.length()").value(2));
        executar(post("/checkout"), token, json("{'idEndereco':%d,'metodoPagamento':'PIX'}", endereco.getId()))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Mesclar lista com o mesmo produto repetido")
    void mesclarProdutoRepetido() throws Exception {
        Cliente cliente = criarCliente();
        Produto p1 = criarProduto(criarVendedor(), "10.00", 10);
        String token = login(cliente);

        executar(post("/carrinhos/meu/mesclar"), token, json(
                "{'itens':[{'idProduto':%d,'quantidade':1},{'idProduto':%d,'quantidade':2}]}", p1.getId(), p1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itens.length()").value(1))
                .andExpect(jsonPath("$.itens[0].quantidade").value(3));
    }

    @Test
    @DisplayName("Comprar de novo o mesmo produto depois de um checkout")
    void comprarDeNovoOMesmoProduto() throws Exception {
        Cliente cliente = criarCliente();
        Endereco endereco = criarEndereco(cliente);
        Produto p1 = criarProduto(criarVendedor(), "10.00", 10);
        String token = login(cliente);

        for (int i = 0; i < 2; i++) {
            executar(post("/carrinhos/meu/itens"), token, json("{'idProduto':%d,'quantidade':1}", p1.getId()))
                    .andExpect(status().isOk());
            executar(post("/checkout"), token, json("{'idEndereco':%d,'metodoPagamento':'PIX'}", endereco.getId()))
                    .andExpect(status().isCreated());
        }
    }

    @Test
    @DisplayName("Duas requisições simultâneas adicionando o mesmo produto")
    void adicionarSimultaneo() throws Exception {
        Cliente cliente = criarCliente();
        Produto p1 = criarProduto(criarVendedor(), "10.00", 10);
        String token = login(cliente);
        executar(get("/carrinhos/meu"), token).andExpect(status().isOk());

        ExecutorService pool = Executors.newFixedThreadPool(4);
        List<Callable<Integer>> tarefas = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            tarefas.add(() -> executar(post("/carrinhos/meu/itens"), token,
                    json("{'idProduto':%d,'quantidade':1}", p1.getId())).andReturn().getResponse().getStatus());
        }
        List<Integer> statusRecebidos = new ArrayList<>();
        for (Future<Integer> f : pool.invokeAll(tarefas)) {
            statusRecebidos.add(f.get());
        }
        pool.shutdown();

        assertThat(statusRecebidos).allMatch(s -> s == 200);
        executar(get("/carrinhos/meu"), token)
                .andExpect(jsonPath("$.itens.length()").value(1))
                .andExpect(jsonPath("$.itens[0].quantidade").value(4));
    }
}
