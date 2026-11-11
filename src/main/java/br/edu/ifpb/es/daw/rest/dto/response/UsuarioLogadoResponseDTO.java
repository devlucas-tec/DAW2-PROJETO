package br.edu.ifpb.es.daw.rest.dto.response;

public record UsuarioLogadoResponseDTO(
        Long id,
        String nome,
        String email,
        String role
) {}
