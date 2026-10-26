package br.edu.ifpb.es.daw.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@NoArgsConstructor
@Entity
@Table(name = "cliente")
@PrimaryKeyJoinColumn(name = "id")
@DiscriminatorValue("CLIENTE")
public class Cliente extends Usuario {

    @Column(length = 20)
    private String telefone;

    @ToString.Exclude
    @OneToMany(mappedBy = "cliente")
    private List<Avaliacao> avaliacoes = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "cliente")
    private List<Pedido> pedidos = new ArrayList<>();

    @ToString.Exclude
    @OneToOne(mappedBy = "cliente", cascade = CascadeType.ALL)
    private Carrinho carrinho;
}
