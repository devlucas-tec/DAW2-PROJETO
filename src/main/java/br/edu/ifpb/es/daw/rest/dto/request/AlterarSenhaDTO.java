package br.edu.ifpb.es.daw.rest.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AlterarSenhaDTO {

    @NotBlank
    @Schema(example = "senha123")
    private String senhaAtual;

    @NotBlank
    @Size(min = 6)
    @Schema(example = "novaSenha123")
    private String novaSenha;
}
