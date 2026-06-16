package br.edu.ifpb.es.daw.rest.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AuthRequestDTO(
        @Schema(example = "poatan@gmail.com")
        @NotBlank @Email String email,

        @Schema(example = "senha123")
        @NotBlank String senha
) {}