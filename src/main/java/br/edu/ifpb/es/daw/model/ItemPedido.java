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
@Table(name = "item_pedido")
public class ItemPedido {

    @EmbeddedId
    private ItemPedidoId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idPedido") // Nome do atributo dentro de ItemPedidoId
    @JoinColumn(name = "id_pedido")
    private Pedido pedido;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idProduto") // Nome do atributo dentro de ItemPedidoId
    @JoinColumn(name = "id_produto")
    private Produto produto;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(name = "preco_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal precoUnitario;
}