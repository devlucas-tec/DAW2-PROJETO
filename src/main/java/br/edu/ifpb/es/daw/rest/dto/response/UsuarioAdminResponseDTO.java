package br.edu.ifpb.es.daw.rest.dto.response;

import java.time.LocalDateTime;

public record UsuarioAdminResponseDTO(
        Long id,
        String nome,
        String email,
        String role,
        boolean ativo,
        LocalDateTime dataCadastro
) {}
