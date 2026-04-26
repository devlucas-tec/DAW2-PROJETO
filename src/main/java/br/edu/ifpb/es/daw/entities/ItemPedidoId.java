package br.edu.ifpb.es.daw.entities;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ItemPedidoId implements Serializable {

    private Long idPedido;

    private Long idProduto;

    public ItemPedidoId() {}

    public ItemPedidoId(Long pedido, Long produto) {
        this.idPedido = pedido;
        this.idProduto = produto;
    }

    public Long getPedido() {
        return idPedido;
    }

    public void setPedido(Long pedido) {
        this.idPedido = pedido;
    }

    public Long getProduto() {
        return idProduto;
    }

    public void setProduto(Long produto) {
        this.idProduto = produto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemPedidoId that = (ItemPedidoId) o;
        return Objects.equals(idPedido, that.idPedido) && Objects.equals(idProduto, that.idProduto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPedido, idProduto);
    }

    public void setIdPedido(Long id) {
    }

    public void setIdProduto(Long id) {

    }
}
