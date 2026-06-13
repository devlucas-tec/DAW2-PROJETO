package br.edu.ifpb.es.daw.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "item_carrinho")
public class ItemCarrinho {

    @EmbeddedId
    private ItemCarrinhoId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("carrinhoId")
    @JoinColumn(name = "carrinho_id")
    private Carrinho carrinho;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("produtoId")
    @JoinColumn(name = "produto_id")
    private Produto produto;

    @Column(name = "preco_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal precoUnitario;

    @Column(nullable = false)
    private Integer quantidade;
}