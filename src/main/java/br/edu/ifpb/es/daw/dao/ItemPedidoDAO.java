package br.edu.ifpb.es.daw.dao;

import br.edu.ifpb.es.daw.model.ItemPedido;

public interface ItemPedidoDAO extends DAO<ItemPedido> {
    ItemPedido findByPedidoAndProduto(Long pedidoId, Long produtoId);
}