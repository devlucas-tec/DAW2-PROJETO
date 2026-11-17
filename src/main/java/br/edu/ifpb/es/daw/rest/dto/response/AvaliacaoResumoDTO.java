package br.edu.ifpb.es.daw.rest.dto.response;

import java.util.Map;

/**
 * Resumo das avaliações de um produto, para o histograma da página do produto.
 * {@code distribuicao} sempre traz as chaves 5, 4, 3, 2 e 1 (nota → quantidade).
 */
public record AvaliacaoResumoDTO(Long idProduto, Double media, long total, Map<Integer, Long> distribuicao) {}
