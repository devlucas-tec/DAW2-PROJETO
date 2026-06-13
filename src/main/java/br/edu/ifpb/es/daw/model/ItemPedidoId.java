package br.edu.ifpb.es.daw.model;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class ItemPedidoId implements Serializable {

    private Long idPedido;

    private Long idProduto;


}