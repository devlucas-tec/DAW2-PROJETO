package br.edu.ifpb.es.daw.model;

import br.edu.ifpb.es.daw.model.enums.EtapaPedido;
import br.edu.ifpb.es.daw.model.enums.StatusPagamento;
import br.edu.ifpb.es.daw.model.enums.StatusPedido;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@Entity
@Table(name = "pedido")
public class Pedido {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id_pedido")
    private Long id;

    @Column(name = "data_pedido", nullable = false, updatable = false)
    private LocalDateTime dataPedido;

    @Column(name = "valor_total", precision = 10, scale = 2, nullable = false)
    private BigDecimal valorTotal = BigDecimal.ZERO;

    @ColumnDefault("0")
    @Column(name = "valor_desconto", precision = 10, scale = 2, nullable = false)
    private BigDecimal valorDesconto = BigDecimal.ZERO;

    @Column(length = 50, nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusPedido status;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_compra")
    private Compra compra;

    @Embedded
    private EnderecoEntrega enderecoEntrega;

    @ToString.Exclude
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedido> itens = new ArrayList<>();

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cupom")
    private Cupom cupom;

    @ToString.Exclude
    @OneToOne(mappedBy = "pedido", fetch = FetchType.LAZY)
    private Pagamento pagamento;

    @ToString.Exclude
    @OneToOne(mappedBy = "pedido", fetch = FetchType.LAZY)
    private Entrega entrega;

    @ToString.Exclude
    @OneToMany(mappedBy = "pedido")
    private List<Devolucao> devolucoes = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("data ASC, id ASC")
    private List<PedidoHistorico> historico = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        if (this.dataPedido == null) this.dataPedido = LocalDateTime.now();
        if (this.status == null) this.status = StatusPedido.CRIADO;
        if (this.valorTotal == null) this.valorTotal = BigDecimal.ZERO;
        if (this.valorDesconto == null) this.valorDesconto = BigDecimal.ZERO;
    }

    public BigDecimal getSubtotal() {
        return itens.stream()
                .map(ItemPedido::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Recalcula o total. Em um pedido avulso (sem compra) o desconto é o valor do cupom;
     * em um pedido gerado pelo checkout o desconto já vem rateado entre os pedidos da compra.
     */
    public void recalcularValorTotal() {
        BigDecimal subtotal = getSubtotal();
        if (compra == null) {
            valorDesconto = cupom != null ? cupom.getValorDesconto().min(subtotal) : BigDecimal.ZERO;
        }
        if (valorDesconto == null) {
            valorDesconto = BigDecimal.ZERO;
        }
        this.valorTotal = subtotal.subtract(valorDesconto).max(BigDecimal.ZERO);
    }

    /** Etapa em que o pedido está agora, do ponto de vista do cliente. */
    public EtapaPedido getEtapaAtual() {
        if (status == null) {
            return null;
        }
        return switch (status) {
            case CRIADO -> EtapaPedido.PEDIDO_CRIADO;
            case CANCELADO -> EtapaPedido.CANCELADO;
            case DEVOLVIDO -> EtapaPedido.DEVOLVIDO;
            case CONFIRMADO -> {
                if (entrega != null) {
                    yield EtapaPedido.de(entrega.getStatusEntrega());
                }
                if (pagamento != null && pagamento.getStatus() == StatusPagamento.APROVADO) {
                    yield EtapaPedido.PAGAMENTO_APROVADO;
                }
                yield EtapaPedido.PEDIDO_CONFIRMADO;
            }
        };
    }

    /**
     * Próxima etapa que o vendedor pode registrar, ou null se não há transição possível
     * (pedido não confirmado, aguardando o cliente pagar, já entregue, cancelado...).
     */
    public EtapaPedido getProximaEtapa() {
        if (status != StatusPedido.CONFIRMADO) {
            return null;
        }
        if (pagamento == null) {
            return null;
        }
        if (pagamento.getStatus() == StatusPagamento.PENDENTE) {
            return EtapaPedido.PAGAMENTO_APROVADO;
        }
        if (pagamento.getStatus() != StatusPagamento.APROVADO) {
            return null;
        }
        if (entrega == null) {
            return EtapaPedido.EMBALADO;
        }
        return switch (entrega.getStatusEntrega()) {
            case EMBALADO -> EtapaPedido.EM_TRANSITO;
            case EM_TRANSITO -> EtapaPedido.SAIU_PARA_ENTREGA;
            case SAIU_PARA_ENTREGA -> EtapaPedido.ENTREGUE;
            case ENTREGUE -> null;
        };
    }
}
