package br.edu.ifpb.es.daw;

import br.edu.ifpb.es.daw.model.*;
import br.edu.ifpb.es.daw.model.enums.StatusCupom;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Carrinho do usuário logado e checkout dividido por vendedor")
class CarrinhoCheckoutTest extends IntegracaoTestBase {

    @Test
    @DisplayName("Adicionar o mesmo produto duas vezes soma; PUT define; acima do estoque é 422")
    void carrinhoSomaEValidaEstoque() throws Exception {
        Cliente cliente = criarCliente();
        Produto produto = criarProduto(criarVendedor(), "25.00", 4);
        String token = login(cliente);

        executar(get("/carrinhos/meu"), token)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCliente").value(cliente.getId()))
                .andExpect(jsonPath("$.quantidadeItens").value(0));

        String item = json("{'idProduto':%d,'quantidade':1}", produto.getId());
        executar(post("/carrinhos/meu/itens"), token, item).andExpect(status().isOk());
        executar(post("/carrinhos/meu/itens"), token, json("{'idProduto':%d,'quantidade':2}", produto.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itens.length()").value(1))
                .andExpect(jsonPath("$.itens[0].quantidade").value(3))
                .andExpect(jsonPath("$.itens[0].subtotal").value(75.0))
                .andExpect(jsonPath("$.valorTotal").value(75.0))
                .andExpect(jsonPath("$.quantidadeItens").value(3));

        // 3 no carrinho + 2 = 5 > estoque 4
        executar(post("/carrinhos/meu/itens"), token, json("{'idProduto':%d,'quantidade':2}", produto.getId()))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.detail", containsString("Estoque insuficiente")));
        executar(put("/carrinhos/meu/itens/" + produto.getId()), token, "{\"quantidade\": 9}")
                .andExpect(status().isUnprocessableEntity());

        executar(put("/carrinhos/meu/itens/" + produto.getId()), token, "{\"quantidade\": 1}")
                .andExpect(jsonPath("$.itens[0].quantidade").value(1));
        executar(delete("/carrinhos/meu/itens/" + produto.getId()), token)
                .andExpect(jsonPath("$.itens.length()").value(0));
    }

    @Test
    @DisplayName("Produto inativo não pode ir para o carrinho")
    void produtoInativoNoCarrinho() throws Exception {
        Produto inativo = criarProduto(criarVendedor(), "25.00", 4, false);
        executar(post("/carrinhos/meu/itens"), login(criarCliente()), json("{'idProduto':%d,'quantidade':1}", inativo.getId()))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @DisplayName("Um cliente não acessa o carrinho de outro, e vendedor não tem carrinho")
    void carrinhoDeOutroCliente() throws Exception {
        Cliente dono = criarCliente();
        Integer idCarrinho = ler(executar(get("/carrinhos/meu"), login(dono)), "$.id");

        executar(get("/carrinhos/" + idCarrinho), login(criarCliente())).andExpect(status().isForbidden());
        executar(get("/carrinhos/meu"), login(criarVendedor())).andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Carrinho com produtos de dois vendedores gera dois pedidos na mesma compra")
    void checkoutDivididoPorVendedor() throws Exception {
        Cliente cliente = criarCliente();
        Endereco endereco = criarEndereco(cliente);
        Vendedor vendedorA = criarVendedor();
        Vendedor vendedorB = criarVendedor();
        Produto produtoA = criarProduto(vendedorA, "100.00", 10);
        Produto produtoB = criarProduto(vendedorB, "50.00", 10);
        Cupom cupom = criarCupom("30.00");
        String token = login(cliente);

        executar(post("/carrinhos/meu/itens"), token, json("{'idProduto':%d,'quantidade':2}", produtoA.getId()));
        executar(post("/carrinhos/meu/itens"), token, json("{'idProduto':%d,'quantidade':1}", produtoB.getId()));

        executar(post("/checkout"), token, json("{'idEndereco':%d,'codigoCupom':'%s','metodoPagamento':'CARTAO'}",
                endereco.getId(), cupom.getCodigo()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.quantidadePedidos").value(2))
                .andExpect(jsonPath("$.valorSubtotal").value(250.0))
                .andExpect(jsonPath("$.valorDesconto").value(30.0))
                .andExpect(jsonPath("$.valorTotal").value(220.0))
                .andExpect(jsonPath("$.statusPagamento").value("APROVADO"))
                .andExpect(jsonPath("$.pedidos[*].itens[0].idVendedor", containsInAnyOrder(
                        vendedorA.getId().intValue(), vendedorB.getId().intValue())))
                // desconto rateado: 250 → 200 (A) leva 24,00 e 50 (B) leva 6,00
                .andExpect(jsonPath("$.pedidos[*].valorDesconto", containsInAnyOrder(24.0, 6.0)))
                .andExpect(jsonPath("$.pedidos[0].status").value("CONFIRMADO"))
                .andExpect(jsonPath("$.pedidos[0].etapaAtual").value("PAGAMENTO_APROVADO"))
                .andExpect(jsonPath("$.pedidos[0].enderecoEntrega.cidade").value("Campina Grande"));

        assertThat(produtoRepository.findById(produtoA.getId()).orElseThrow().getEstoque()).isEqualTo(8);
        assertThat(produtoRepository.findById(produtoB.getId()).orElseThrow().getEstoque()).isEqualTo(9);
        executar(get("/carrinhos/meu"), token).andExpect(jsonPath("$.itens.length()").value(0));

        // Cada vendedor só vê o próprio pedido, sem os itens do outro.
        executar(get("/pedidos"), login(vendedorA))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.content[0].itens.length()").value(1))
                .andExpect(jsonPath("$.content[0].itens[0].idProduto").value(produtoA.getId()));
    }

    @Test
    @DisplayName("Falha de estoque no checkout desfaz toda a operação")
    void checkoutRollbackPorEstoque() throws Exception {
        Cliente cliente = criarCliente();
        Endereco endereco = criarEndereco(cliente);
        Produto produtoA = criarProduto(criarVendedor(), "100.00", 10);
        Produto produtoB = criarProduto(criarVendedor(), "50.00", 5);
        String token = login(cliente);

        executar(post("/carrinhos/meu/itens"), token, json("{'idProduto':%d,'quantidade':2}", produtoA.getId()));
        executar(post("/carrinhos/meu/itens"), token, json("{'idProduto':%d,'quantidade':3}", produtoB.getId()));

        // Outro cliente comprou antes: o estoque de B caiu para 1.
        produtoB.setEstoque(1);
        produtoRepository.save(produtoB);

        executar(post("/checkout"), token, json("{'idEndereco':%d,'metodoPagamento':'PIX'}", endereco.getId()))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.detail", containsString(produtoB.getNome())));

        assertThat(produtoRepository.findById(produtoA.getId()).orElseThrow().getEstoque()).isEqualTo(10);
        assertThat(produtoRepository.findById(produtoB.getId()).orElseThrow().getEstoque()).isEqualTo(1);
        assertThat(pedidoRepository.buscarPorCliente(cliente.getId(), null,
                org.springframework.data.domain.Pageable.unpaged()).getTotalElements()).isZero();
        executar(get("/carrinhos/meu"), token).andExpect(jsonPath("$.itens.length()").value(2));
    }

    @Test
    @DisplayName("Checkout com carrinho vazio, endereço de outro cliente ou cupom inválido é recusado")
    void checkoutInvalido() throws Exception {
        Cliente cliente = criarCliente();
        Endereco endereco = criarEndereco(cliente);
        Endereco enderecoDeOutro = criarEndereco(criarCliente());
        Produto produto = criarProduto(criarVendedor(), "10.00", 5);
        String token = login(cliente);

        executar(post("/checkout"), token, json("{'idEndereco':%d,'metodoPagamento':'PIX'}", endereco.getId()))
                .andExpect(status().isUnprocessableEntity());

        executar(post("/carrinhos/meu/itens"), token, json("{'idProduto':%d,'quantidade':1}", produto.getId()));
        executar(post("/checkout"), token, json("{'idEndereco':%d,'metodoPagamento':'PIX'}", enderecoDeOutro.getId()))
                .andExpect(status().isForbidden());
        executar(post("/checkout"), token, json("{'idEndereco':%d,'codigoCupom':'NAOEXISTE','metodoPagamento':'PIX'}", endereco.getId()))
                .andExpect(status().isUnprocessableEntity());
    }

    private Cupom criarCupom(String valor) {
        Cupom cupom = new Cupom();
        cupom.setCodigo("TESTE" + proximo());
        cupom.setValorDesconto(new BigDecimal(valor));
        cupom.setDataExpiracao(LocalDate.now().plusDays(10));
        cupom.setStatus(StatusCupom.ATIVO);
        return cupomRepository.save(cupom);
    }
}
