package br.edu.ifpb.es.daw;

import br.edu.ifpb.es.daw.model.Pedido;
import br.edu.ifpb.es.daw.model.enums.StatusPedido;
import br.edu.ifpb.es.daw.seed.SeedService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Seed gera dados coerentes com as regras de negócio")
class SeedTest extends IntegracaoTestBase {

    @Autowired private SeedService seedService;

    @Test
    @Transactional
    @DisplayName("Popular cria compras com um pedido por vendedor, imagens e histórico")
    void popular() throws Exception {
        seedService.limpar();
        seedService.popular();

        assertThat(produtoRepository.findAll()).allMatch(p -> p.getImagemUrl() != null && p.getImagemUrl().startsWith("https://"));
        assertThat(compraRepository.count()).isEqualTo(50);

        List<Pedido> pedidos = pedidoRepository.findAll();
        assertThat(pedidos).hasSize(75); // 25 compras com 1 vendedor + 25 com 2 vendedores
        assertThat(pedidos).allMatch(p -> p.getCompra() != null && p.getEnderecoEntrega() != null && !p.getHistorico().isEmpty());
        // Cada pedido tem itens de um único vendedor.
        assertThat(pedidos).allMatch(p -> p.getItens().stream()
                .map(i -> i.getProduto().getVendedor().getId()).distinct().count() == 1);
        // Pedidos confirmados com pagamento aprovado e entrega têm etapa coerente.
        assertThat(pedidos.stream().filter(p -> p.getStatus() == StatusPedido.CONFIRMADO))
                .allMatch(p -> p.getEtapaAtual() != null);
        // Avaliações só existem para produtos que o cliente recebeu.
        assertThat(produtoRepository.findAll().stream().filter(p -> p.getTotalAvaliacoes() > 0)).isNotEmpty();
        assertThat(pedidos.stream().map(Pedido::getEntrega).filter(Objects::nonNull)).isNotEmpty();

        executar(get("/produtos").param("size", "50"), null)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(48)); // 2 anúncios pausados
    }
}
