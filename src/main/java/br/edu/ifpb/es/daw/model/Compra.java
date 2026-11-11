package br.edu.ifpb.es.daw.model;

import br.edu.ifpb.es.daw.model.enums.MetodoPagamento;
import br.edu.ifpb.es.daw.model.enums.StatusPagamento;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Agrupa os pedidos gerados por um checkout. O carrinho é dividido em um pedido
 * por vendedor e todos ficam ligados à mesma compra.
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@Entity
@Table(name = "compra")
public class Compra {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(length = 20, nullable = false, unique = true)
    private String codigo;

    @Column(name = "data_compra", nullable = false, updatable = false)
    private LocalDateTime dataCompra;

    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_pagamento", length = 20, nullable = false)
    private MetodoPagamento metodoPagamento;

    @Column(name = "valor_subtotal", precision = 10, scale = 2, nullable = false)
    private BigDecimal valorSubtotal = BigDecimal.ZERO;

    @Column(name = "valor_desconto", precision = 10, scale = 2, nullable = false)
    private BigDecimal valorDesconto = BigDecimal.ZERO;

    @Column(name = "valor_total", precision = 10, scale = 2, nullable = false)
    private BigDecimal valorTotal = BigDecimal.ZERO;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cupom")
    private Cupom cupom;

    @ToString.Exclude
    @OneToMany(mappedBy = "compra")
    @OrderBy("id ASC")
    private List<Pedido> pedidos = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        if (this.dataCompra == null) this.dataCompra = LocalDateTime.now();
    }

    /** APROVADO quando todos os pagamentos foram aprovados; caso contrário, o primeiro status diferente de APROVADO. */
    public StatusPagamento getStatusPagamento() {
        if (pedidos.isEmpty()) {
            return null;
        }
        return pedidos.stream()
                .map(Pedido::getPagamento)
                .filter(Objects::nonNull)
                .map(Pagamento::getStatus)
                .filter(status -> status != StatusPagamento.APROVADO)
                .findFirst()
                .orElse(StatusPagamento.APROVADO);
    }
}
