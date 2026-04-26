package br.edu.ifpb.es.daw.dao;

import br.edu.ifpb.es.daw.entities.ItemCarrinho;

public interface ItemCarrinhoDAO extends DAO<ItemCarrinho> {
    ItemCarrinho findByCarrinhoAndProduto(Long carrinhoId, Long produtoId);
}