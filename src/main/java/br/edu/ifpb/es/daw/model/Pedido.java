package br.edu.ifpb.es.daw.model;

import br.edu.ifpb.es.daw.model.enums.StatusPedido;
import jakarta.persistence.*;
import lombok.*;

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

    @Column(length = 50, nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusPedido status;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

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

    @PrePersist
    protected void onCreate() {
        if (this.dataPedido == null) this.dataPedido = LocalDateTime.now();
        if (this.status == null) this.status = StatusPedido.CRIADO;
        if (this.valorTotal == null) this.valorTotal = BigDecimal.ZERO;
    }

    public void recalcularValorTotal() {
        BigDecimal subtotal = itens.stream()
                .map(ItemPedido::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal desconto = cupom != null ? cupom.getValorDesconto() : BigDecimal.ZERO;
        this.valorTotal = subtotal.subtract(desconto).max(BigDecimal.ZERO);
    }
}
