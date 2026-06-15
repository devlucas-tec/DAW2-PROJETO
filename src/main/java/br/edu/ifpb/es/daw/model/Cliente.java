package br.edu.ifpb.es.daw.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = "cliente")
@PrimaryKeyJoinColumn(name = "id")
@DiscriminatorValue("CLIENTE")
public class Cliente extends Usuario {

    private String telefone;

    // Lista todas as avaliações que o cliente fez
    @OneToMany(mappedBy = "cliente")
    private List<Avaliacao> avaliacoes = new ArrayList<>();

    // Lista todos os pedidos do cliente
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    private List<Pedido> pedidos = new ArrayList<>();

    // Carrinho do cliente
    @OneToOne(mappedBy = "cliente", cascade = CascadeType.ALL)
    private Carrinho carrinho;
}