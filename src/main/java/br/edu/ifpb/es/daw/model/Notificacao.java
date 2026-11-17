package br.edu.ifpb.es.daw.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

/** Aviso para um usuário: novo pedido (vendedor), mudança de status (cliente), devolução etc. */
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@Entity
@Table(name = "notificacao", indexes = @Index(name = "idx_notificacao_usuario", columnList = "id_usuario, lida"))
public class Notificacao {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_usuario", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Usuario usuario;

    @Column(length = 100, nullable = false)
    private String titulo;

    @Column(length = 300, nullable = false)
    private String mensagem;

    /** Rota do front para abrir ao clicar (ex.: /conta/pedidos/10). */
    @Column(length = 200)
    private String link;

    @Column(name = "id_pedido")
    private Long idPedido;

    @Column(nullable = false)
    private boolean lida = false;

    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;

    @PrePersist
    protected void onCreate() {
        if (dataCriacao == null) dataCriacao = LocalDateTime.now();
    }
}
