package br.edu.ifpb.es.daw.exception;

public class EntidadeNaoEncontradaException extends RuntimeException {

    public EntidadeNaoEncontradaException(String entidade, Long id) {
        super(String.format("Entidade '%s' de id '%d' não foi encontrada!", entidade, id));
    }

    public EntidadeNaoEncontradaException(String entidade, String campo, String valor) {
        super(String.format("Entidade '%s' com %s '%s' não foi encontrada!", entidade, campo, valor));
    }
}