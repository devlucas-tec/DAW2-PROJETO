package br.edu.ifpb.es.daw.model;

import br.edu.ifpb.es.daw.model.enums.StatusDevolucao;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "devolucao")
public class Devolucao {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "data_devolucao", nullable = false, updatable = false)
    private LocalDateTime dataDevolucao;

    @Column(nullable = false, length = 1000)
    private String motivo;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private StatusDevolucao status = StatusDevolucao.APROVADA;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pedido")
    private Pedido pedido;

    @PrePersist
    protected void onCreate() {
        this.dataDevolucao = LocalDateTime.now();
    }

}