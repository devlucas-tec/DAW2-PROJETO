package br.edu.ifpb.es.daw.service;

import br.edu.ifpb.es.daw.model.*;
import br.edu.ifpb.es.daw.model.enums.Role;
import br.edu.ifpb.es.daw.model.enums.StatusPedido;
import br.edu.ifpb.es.daw.repository.*;
import br.edu.ifpb.es.daw.rest.dto.response.DashboardDTOs.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Métricas dos painéis. Só contam como venda os pedidos CONFIRMADOS
 * (cancelados, devolvidos e pedidos avulsos ainda não fechados ficam de fora).
 */
@Service
@Transactional(readOnly = true)
public class DashboardService {

    private static final int TOP = 5;
    private static final int LIMITE_ESTOQUE_BAIXO = 5;

    @Autowired private ItemPedidoRepository itemPedidoRepository;
    @Autowired private PedidoRepository pedidoRepository;
    @Autowired private ProdutoRepository produtoRepository;
    @Autowired private AvaliacaoRepository avaliacaoRepository;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private UsuarioAutenticadoService usuarioAutenticado;

    /** Período aceito pelos dashboards: 7d e 30d (série diária) ou 12m (série mensal). */
    public enum Periodo {
        DIAS_7("7d", 7, false),
        DIAS_30("30d", 30, false),
        MESES_12("12m", 12, true);

        private final String codigo;
        private final int quantidade;
        private final boolean mensal;

        Periodo(String codigo, int quantidade, boolean mensal) {
            this.codigo = codigo;
            this.quantidade = quantidade;
            this.mensal = mensal;
        }

        public static Periodo de(String codigo) {
            String valor = codigo == null || codigo.isBlank() ? "30d" : codigo.trim().toLowerCase();
            return Arrays.stream(values())
                    .filter(p -> p.codigo.equals(valor))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Período '" + codigo + "' inválido. Valores aceitos: 7d, 30d, 12m."));
        }

        LocalDateTime inicio() {
            return mensal
                    ? YearMonth.now().minusMonths(quantidade - 1L).atDay(1).atStartOfDay()
                    : LocalDate.now().minusDays(quantidade - 1L).atStartOfDay();
        }

        String rotulo(LocalDateTime data) {
            return mensal ? YearMonth.from(data).toString() : data.toLocalDate().toString();
        }

        List<String> rotulos() {
            List<String> rotulos = new ArrayList<>();
            LocalDateTime inicio = inicio();
            for (int i = 0; i < quantidade; i++) {
                rotulos.add(rotulo(mensal ? inicio.plusMonths(i) : inicio.plusDays(i)));
            }
            return rotulos;
        }
    }

    public DashboardVendedor vendedor(String codigoPeriodo) {
        Periodo periodo = Periodo.de(codigoPeriodo);
        Long idVendedor = usuarioAutenticado.getUsuario().getId();
        LocalDateTime inicio = periodo.inicio();

        List<ItemPedido> itens = itemPedidoRepository.buscarDoVendedorDesde(idVendedor, inicio);
        List<ItemPedido> vendidos = itens.stream()
                .filter(item -> item.getPedido().getStatus() == StatusPedido.CONFIRMADO)
                .toList();

        BigDecimal faturamento = somar(vendidos);
        long totalPedidos = vendidos.stream().map(item -> item.getPedido().getId()).distinct().count();

        Collection<Pedido> pedidosDoPeriodo = itens.stream()
                .map(ItemPedido::getPedido)
                .collect(Collectors.toMap(Pedido::getId, Function.identity(), (a, b) -> a, LinkedHashMap::new))
                .values();

        Map<String, Long> pedidosPorEtapa = new TreeMap<>();
        pedidosDoPeriodo.forEach(pedido -> pedidosPorEtapa.merge(String.valueOf(pedido.getEtapaAtual()), 1L, Long::sum));

        Map<String, BigDecimal> faturamentoPorBucket = new HashMap<>();
        Map<String, Set<Long>> pedidosPorBucket = new HashMap<>();
        for (ItemPedido item : vendidos) {
            String rotulo = periodo.rotulo(item.getPedido().getDataPedido());
            faturamentoPorBucket.merge(rotulo, item.getSubtotal(), BigDecimal::add);
            pedidosPorBucket.computeIfAbsent(rotulo, r -> new HashSet<>()).add(item.getPedido().getId());
        }
        List<PontoSerie> serie = periodo.rotulos().stream()
                .map(rotulo -> new PontoSerie(rotulo,
                        faturamentoPorBucket.getOrDefault(rotulo, BigDecimal.ZERO),
                        pedidosPorBucket.getOrDefault(rotulo, Set.of()).size(),
                        0))
                .toList();

        List<ProdutoVendido> topProdutos = vendidos.stream()
                .collect(Collectors.groupingBy(item -> item.getProduto().getId(), LinkedHashMap::new, Collectors.toList()))
                .values().stream()
                .map(grupo -> {
                    Produto produto = grupo.get(0).getProduto();
                    return new ProdutoVendido(produto.getId(), produto.getNome(), produto.getImagemUrl(),
                            grupo.stream().mapToLong(ItemPedido::getQuantidade).sum(), somar(grupo));
                })
                .sorted(Comparator.comparingLong(ProdutoVendido::quantidadeVendida).reversed()
                        .thenComparing(ProdutoVendido::faturamento, Comparator.reverseOrder()))
                .limit(TOP)
                .toList();

        List<ProdutoEstoqueBaixo> estoqueBaixo = produtoRepository
                .findByVendedorIdAndEstoqueLessThanEqualOrderByEstoqueAscNomeAsc(idVendedor, LIMITE_ESTOQUE_BAIXO)
                .stream()
                .map(p -> new ProdutoEstoqueBaixo(p.getId(), p.getNome(), p.getEstoque(), p.isAtivo()))
                .toList();

        long totalAvaliacoes = avaliacaoRepository.countByProdutoVendedorId(idVendedor);
        Double media = avaliacaoRepository.mediaPorVendedor(idVendedor);

        return new DashboardVendedor(
                periodo.codigo, inicio, LocalDateTime.now(),
                faturamento, totalPedidos, ticketMedio(faturamento, totalPedidos),
                contarPorStatus(pedidosDoPeriodo), pedidosPorEtapa,
                serie, topProdutos, estoqueBaixo,
                totalAvaliacoes == 0 || media == null ? null : AvaliacaoService.arredondar(media),
                totalAvaliacoes);
    }

    public DashboardAdmin admin(String codigoPeriodo) {
        Periodo periodo = Periodo.de(codigoPeriodo);
        LocalDateTime inicio = periodo.inicio();

        List<Pedido> pedidos = pedidoRepository.findByDataPedidoGreaterThanEqual(inicio);
        List<Pedido> confirmados = pedidos.stream().filter(p -> p.getStatus() == StatusPedido.CONFIRMADO).toList();
        BigDecimal gmv = confirmados.stream().map(Pedido::getValorTotal).reduce(BigDecimal.ZERO, BigDecimal::add);

        List<Usuario> novos = usuarioRepository.findByDataCadastroGreaterThanEqual(inicio);
        NovosUsuarios novosUsuarios = new NovosUsuarios(
                novos.size(),
                novos.stream().filter(u -> u.getRole() == Role.CLIENTE).count(),
                novos.stream().filter(u -> u.getRole() == Role.VENDEDOR).count());

        List<ItemPedido> itensVendidos = itemPedidoRepository.buscarDesde(inicio).stream()
                .filter(item -> item.getPedido().getStatus() == StatusPedido.CONFIRMADO)
                .toList();

        List<VendedorRanking> topVendedores = itensVendidos.stream()
                .collect(Collectors.groupingBy(item -> item.getProduto().getVendedor().getId()))
                .values().stream()
                .map(grupo -> {
                    Vendedor vendedor = grupo.get(0).getProduto().getVendedor();
                    long qtdPedidos = grupo.stream().map(item -> item.getPedido().getId()).distinct().count();
                    return new VendedorRanking(vendedor.getId(), vendedor.getRazaoSocial(), qtdPedidos, somar(grupo));
                })
                .sorted(Comparator.comparing(VendedorRanking::faturamento).reversed())
                .limit(TOP)
                .toList();

        List<CategoriaRanking> topCategorias = itensVendidos.stream()
                .collect(Collectors.groupingBy(item -> item.getProduto().getCategoria().getId()))
                .values().stream()
                .map(grupo -> {
                    Categoria categoria = grupo.get(0).getProduto().getCategoria();
                    return new CategoriaRanking(categoria.getId(), categoria.getNome(),
                            grupo.stream().mapToLong(ItemPedido::getQuantidade).sum(), somar(grupo));
                })
                .sorted(Comparator.comparing(CategoriaRanking::faturamento).reversed())
                .limit(TOP)
                .toList();

        Map<String, BigDecimal> gmvPorBucket = new HashMap<>();
        Map<String, Long> pedidosPorBucket = new HashMap<>();
        for (Pedido pedido : confirmados) {
            String rotulo = periodo.rotulo(pedido.getDataPedido());
            gmvPorBucket.merge(rotulo, pedido.getValorTotal(), BigDecimal::add);
            pedidosPorBucket.merge(rotulo, 1L, Long::sum);
        }
        Map<String, Long> usuariosPorBucket = novos.stream()
                .collect(Collectors.groupingBy(u -> periodo.rotulo(u.getDataCadastro()), Collectors.counting()));
        List<PontoSerie> serie = periodo.rotulos().stream()
                .map(rotulo -> new PontoSerie(rotulo,
                        gmvPorBucket.getOrDefault(rotulo, BigDecimal.ZERO),
                        pedidosPorBucket.getOrDefault(rotulo, 0L),
                        usuariosPorBucket.getOrDefault(rotulo, 0L)))
                .toList();

        return new DashboardAdmin(
                periodo.codigo, inicio, LocalDateTime.now(),
                gmv, confirmados.size(), ticketMedio(gmv, confirmados.size()),
                contarPorStatus(pedidos), novosUsuarios, topVendedores, topCategorias, serie);
    }

    private Map<String, Long> contarPorStatus(Collection<Pedido> pedidos) {
        Map<String, Long> contagem = new LinkedHashMap<>();
        for (StatusPedido status : StatusPedido.values()) {
            contagem.put(status.name(), 0L);
        }
        pedidos.forEach(pedido -> contagem.merge(pedido.getStatus().name(), 1L, Long::sum));
        return contagem;
    }

    private BigDecimal somar(Collection<ItemPedido> itens) {
        return itens.stream().map(ItemPedido::getSubtotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal ticketMedio(BigDecimal total, long quantidade) {
        return quantidade == 0
                ? BigDecimal.ZERO
                : total.divide(BigDecimal.valueOf(quantidade), 2, RoundingMode.HALF_UP);
    }
}
