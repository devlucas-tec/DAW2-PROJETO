package br.edu.ifpb.es.daw.dao;

import br.edu.ifpb.es.daw.model.Produto;

import java.math.BigDecimal;
import java.util.List;

public interface ProdutoDAO extends DAO<Produto> {

    /**
     * 2.1 - Consulta parametrizada com um parâmetro de tipo primitivo/wrapper (String).
     * Retorna todos os produtos cujo nome contenha o trecho informado.
     *
     * @param nome trecho do nome a ser buscado
     * @return lista de produtos encontrados
     */
    List<Produto> findByNome(String nome) throws PersistenciaDawException;

    /**
     * 2.3 - Consulta parametrizada com dois parâmetros de tipos primitivos/wrappers (BigDecimal).
     * Retorna todos os produtos com preço dentro da faixa informada.
     *
     * @param precoMin preço mínimo (inclusive)
     * @param precoMax preço máximo (inclusive)
     * @return lista de produtos encontrados
     */
    List<Produto> findByFaixaDePreco(BigDecimal precoMin, BigDecimal precoMax) throws PersistenciaDawException;

}