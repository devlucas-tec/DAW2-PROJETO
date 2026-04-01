package br.edu.ifpb.es.daw.entities;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "item_carrinho")
public class ItemCarrinho {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private Integer quantidade;

    public ItemCarrinho() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ItemCarrinho that = (ItemCarrinho) o;
        return Objects.equals(id, that.id) && Objects.equals(quantidade, that.quantidade);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quantidade);
    }

    @Override
    public String toString() {
        return "ItemCarrinho{" +
                "id=" + id +
                ", quantidade=" + quantidade +
                '}';
    }
}
