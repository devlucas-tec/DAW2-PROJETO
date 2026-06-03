package br.edu.ifpb.es.daw.dao;

import br.edu.ifpb.es.daw.model.Cliente;
import br.edu.ifpb.es.daw.model.Pedido;

import java.util.List;

public interface PedidoDAO extends DAO<Pedido> {

    /**
     *
     * 2.2 - Consulta parametrizada onde o parâmetro é uma entidade (Cliente).
     * Retorna todos os pedidos associados ao cliente informado.
     *
     * @param cliente entidade Cliente a ser usada como parâmetro da consulta
     * @return lista de pedidos do cliente
     */
    List<Pedido> findByCliente(Cliente cliente) throws PersistenciaDawException;

    /**
     * 2.5 - Consulta que faz FETCH de relacionamento LAZY (itens do pedido).
     * Retorna o pedido com a lista de itens já carregada, evitando LazyInitializationException.
     *
     * @param id identificador do pedido
     * @return pedido com itens carregados via JOIN FETCH
     */
    Pedido findByIdComItens(Long id) throws PersistenciaDawException;

}