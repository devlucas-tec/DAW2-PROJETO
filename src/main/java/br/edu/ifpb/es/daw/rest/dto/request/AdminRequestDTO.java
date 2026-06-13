package br.edu.ifpb.es.daw.rest.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AdminRequestDTO {

    @NotBlank
    @Schema(example = "Admin")
    private String nome;

    @NotBlank
    @Email
    @Schema(example = "admin@gmail.com")
    private String email;

    @NotBlank
    @Size(min = 6)
    @Schema(example = "senha123")
    private String senha;
}