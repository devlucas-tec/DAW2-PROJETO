package br.edu.ifpb.es.daw.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "carrinho")
public class Carrinho {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDate dataCriacao;

    @Column(name = "data_ultima_atualizacao", nullable = false)
    private LocalDate dataAtualizacao;

    // Vínculo com Cliente (Um cliente tem um carrinho)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    // Itens do Carrinho
    @OneToMany(mappedBy = "carrinho", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemCarrinho> itens = new ArrayList<>();


    @PrePersist
    protected void onCreate() {
        this.dataCriacao = LocalDate.now();
        this.dataAtualizacao = LocalDate.now();
    }

    @PreUpdate
    protected void onUpdate() {

        this.dataAtualizacao = LocalDate.now();
    }


}
