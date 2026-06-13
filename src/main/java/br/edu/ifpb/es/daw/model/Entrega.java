package br.edu.ifpb.es.daw.model;

import br.edu.ifpb.es.daw.model.enums.StatusEntrega;
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
@Table(name = "entrega")
public class Entrega {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(length = 50)
    private String transportadora;

    @Column(name = "codigo_rastreamento", length = 50, unique = true)
    private String codigoRastreamento;

    @Column(name = "status_entrega", nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusEntrega statusEntrega;

    @Column(name = "data_envio")
    private LocalDateTime dataEnvio;

    @Column(name = "data_entrega_prevista")
    private LocalDateTime dataEntregaPrevista;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pedido")
    private Pedido pedido;

    @PrePersist
    protected void onCreate() {
        this.dataEnvio = LocalDateTime.now();
        this.dataEntregaPrevista = LocalDateTime.now().plusDays(7);
    }

    @PreUpdate
    protected void onUpdate() {
        this.dataEntregaPrevista = LocalDateTime.now().plusDays(7);
    }
}