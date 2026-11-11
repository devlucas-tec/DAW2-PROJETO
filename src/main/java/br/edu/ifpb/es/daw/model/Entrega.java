package br.edu.ifpb.es.daw.model;

import br.edu.ifpb.es.daw.model.enums.StatusEntrega;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@Entity
@Table(name = "entrega")
public class Entrega {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(length = 50)
    private String transportadora;

    @Column(name = "codigo_rastreamento", length = 50, unique = true)
    private String codigoRastreamento;

    @Column(name = "status_entrega", nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusEntrega statusEntrega = StatusEntrega.EMBALADO;

    @Column(name = "data_envio")
    private LocalDateTime dataEnvio;

    @Column(name = "data_entrega_prevista")
    private LocalDateTime dataEntregaPrevista;

    @Column(name = "data_entrega")
    private LocalDateTime dataEntrega;

    @ToString.Exclude
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pedido", nullable = false, unique = true)
    private Pedido pedido;

    @PrePersist
    protected void onCreate() {
        // dataEnvio é preenchida quando a entrega passa para EM_TRANSITO.
        if (this.dataEntregaPrevista == null) {
            this.dataEntregaPrevista = (this.dataEnvio != null ? this.dataEnvio : LocalDateTime.now()).plusDays(7);
        }
    }
}
