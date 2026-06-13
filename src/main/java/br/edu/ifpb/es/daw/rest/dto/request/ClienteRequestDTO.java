package br.edu.ifpb.es.daw.rest.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ClienteRequestDTO {

    @NotBlank
    @Schema(example = "Alex Pereira")
    private String nome;

    @NotBlank
    @Email
    @Schema(example = "poatan@gmail.com")
    private String email;

    @NotBlank
    @Schema(example = "senha123")
    private String senha;

    @Schema(example = "83999990001")
    private String telefone;
}