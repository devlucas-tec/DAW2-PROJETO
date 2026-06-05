package br.edu.ifpb.es.daw.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class ItemCarrinhoId implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name = "carrinho_id", nullable = false)
    private Long carrinhoId;

    @Column(name = "produto_id", nullable = false)
    private Long produtoId;
}