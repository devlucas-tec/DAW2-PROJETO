package br.edu.ifpb.es.daw.model;

import br.edu.ifpb.es.daw.model.enums.StatusDevolucao;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@Entity
@Table(name = "devolucao")
public class Devolucao {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "data_devolucao", nullable = false, updatable = false)
    private LocalDateTime dataDevolucao;

    @Column(nullable = false, length = 1000)
    private String motivo;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private StatusDevolucao status = StatusDevolucao.PENDENTE;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pedido", nullable = false)
    private Pedido pedido;

    @PrePersist
    protected void onCreate() {
        if (this.dataDevolucao == null) this.dataDevolucao = LocalDateTime.now();
    }
}
