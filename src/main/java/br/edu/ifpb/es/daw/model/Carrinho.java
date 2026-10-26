package br.edu.ifpb.es.daw.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@Entity
@Table(name = "carrinho")
public class Carrinho {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDate dataCriacao;

    @Column(name = "data_ultima_atualizacao", nullable = false)
    private LocalDate dataAtualizacao;

    @ToString.Exclude
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente", nullable = false, unique = true)
    private Cliente cliente;

    @ToString.Exclude
    @OneToMany(mappedBy = "carrinho", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemCarrinho> itens = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        if (this.dataCriacao == null) this.dataCriacao = LocalDate.now();
        if (this.dataAtualizacao == null) this.dataAtualizacao = LocalDate.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.dataAtualizacao = LocalDate.now();
    }
}
