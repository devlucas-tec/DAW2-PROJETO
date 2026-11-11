package br.edu.ifpb.es.daw.model;

import br.edu.ifpb.es.daw.model.enums.EtapaPedido;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Registro de cada etapa pela qual o pedido passou (timeline do cliente).
 * O autor é guardado como cópia (id, nome e papel) para não depender de o usuário continuar existindo.
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@Entity
@Table(name = "pedido_historico")
public class PedidoHistorico {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pedido", nullable = false)
    private Pedido pedido;

    @Enumerated(EnumType.STRING)
    @Column(length = 30, nullable = false)
    private EtapaPedido etapa;

    @Column(nullable = false)
    private LocalDateTime data;

    @Column(length = 255)
    private String descricao;

    @Column(name = "id_autor")
    private Long idAutor;

    @Column(name = "nome_autor", length = 100)
    private String nomeAutor;

    @Column(name = "papel_autor", length = 20)
    private String papelAutor;

    @PrePersist
    protected void onCreate() {
        if (this.data == null) this.data = LocalDateTime.now();
    }
}
