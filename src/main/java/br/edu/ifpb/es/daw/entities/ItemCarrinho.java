package br.edu.ifpb.es.daw.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "item_carrinho")
public class ItemCarrinho {

    private Integer quantidade;
}
