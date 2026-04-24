package br.edu.ifpb.es.daw.entities;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ItemCarrinhoId implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name = "carrinho_id", nullable = false)
    private Long carrinhoId;

    @Column(name = "produto_id", nullable = false)
    private Long produtoId;

    // Default constructor
    public ItemCarrinhoId() {
    }

    // Constructor with all fields
    public ItemCarrinhoId(Long carrinhoId, Long produtoId) {
        this.carrinhoId = carrinhoId;
        this.produtoId = produtoId;
    }

    // Getters and Setters
    public Long getCarrinhoId() {
        return carrinhoId;
    }

    public void setCarrinhoId(Long carrinhoId) {
        this.carrinhoId = carrinhoId;
    }

    public Long getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(Long produtoId) {
        this.produtoId = produtoId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ItemCarrinhoId that = (ItemCarrinhoId) obj;
        return Objects.equals(carrinhoId, that.carrinhoId) &&
                Objects.equals(produtoId, that.produtoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(carrinhoId, produtoId);
    }
}