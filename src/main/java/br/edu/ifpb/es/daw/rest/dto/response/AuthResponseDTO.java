package br.edu.ifpb.es.daw.rest.dto.response;

public record AuthResponseDTO(
        String token,
        String role,
        String email
) {}