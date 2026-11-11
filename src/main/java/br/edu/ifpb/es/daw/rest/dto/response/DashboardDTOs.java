package br.edu.ifpb.es.daw.rest.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/** Respostas dos dashboards do vendedor e do administrador. */
public final class DashboardDTOs {

    private DashboardDTOs() {
    }

    /** Um ponto da série temporal: dia (yyyy-MM-dd) ou mês (yyyy-MM). */
    public record PontoSerie(String periodo, BigDecimal faturamento, long pedidos, long novosUsuarios) {}

    public record ProdutoVendido(Long idProduto, String nome, String imagemUrl, long quantidadeVendida, BigDecimal faturamento) {}

    public record ProdutoEstoqueBaixo(Long idProduto, String nome, int estoque, boolean ativo) {}

    public record VendedorRanking(Long idVendedor, String nome, long pedidos, BigDecimal faturamento) {}

    public record CategoriaRanking(Long idCategoria, String nome, long quantidadeVendida, BigDecimal faturamento) {}

    public record NovosUsuarios(long total, long clientes, long vendedores) {}

    public record DashboardVendedor(
            String periodo,
            LocalDateTime inicio,
            LocalDateTime fim,
            BigDecimal faturamento,
            long totalPedidos,
            BigDecimal ticketMedio,
            Map<String, Long> pedidosPorStatus,
            Map<String, Long> pedidosPorEtapa,
            List<PontoSerie> serie,
            List<ProdutoVendido> topProdutos,
            List<ProdutoEstoqueBaixo> estoqueBaixo,
            Double mediaAvaliacoes,
            long totalAvaliacoes
    ) {}

    public record DashboardAdmin(
            String periodo,
            LocalDateTime inicio,
            LocalDateTime fim,
            BigDecimal gmv,
            long totalPedidos,
            BigDecimal ticketMedio,
            Map<String, Long> pedidosPorStatus,
            NovosUsuarios novosUsuarios,
            List<VendedorRanking> topVendedores,
            List<CategoriaRanking> topCategorias,
            List<PontoSerie> serie
    ) {}
}
