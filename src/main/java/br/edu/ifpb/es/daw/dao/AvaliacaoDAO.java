package br.edu.ifpb.es.daw.dao;

import br.edu.ifpb.es.daw.entities.Avaliacao;
import br.edu.ifpb.es.daw.entities.Produto;

public interface AvaliacaoDAO extends DAO<Avaliacao> {

    /**
     * 2.4 - Consulta que faz uso de função de agregação (AVG).
     * Retorna a média das notas de todas as avaliações do produto informado.
     *
     * @param produto entidade Produto cujas avaliações serão consideradas
     * @return média das notas do produto, ou null se não houver avaliações
     */
    Double calcularMediaNotasPorProduto(Produto produto) throws PersistenciaDawException;

}