package br.edu.ifpb.es.daw.model;

import br.edu.ifpb.es.daw.model.enums.MetodoPagamento;
import br.edu.ifpb.es.daw.model.enums.StatusPagamento;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "pagamento")
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id_pagamento")
    private Long id;

    @Column(length = 50, nullable = false)
    @Enumerated(EnumType.STRING)
    private MetodoPagamento metodo;

    @Column(length = 50, nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusPagamento status;

    @Column(name = "valor_pago", precision = 10, scale = 2, nullable = false)
    private BigDecimal valorPago;

    @Column(name = "data_pagamento", nullable = false)
    private LocalDateTime dataPagamento;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pedido")
    private Pedido pedido;

    @PrePersist
    protected void onCreate() {
        this.dataPagamento = LocalDateTime.now();
    }
}