package br.edu.ifpb.es.daw.rest.dto.response;

/** Se o cliente logado pode avaliar o produto; quando não pode, {@code motivo} explica por quê. */
public record PodeAvaliarDTO(boolean podeAvaliar, String motivo) {}
