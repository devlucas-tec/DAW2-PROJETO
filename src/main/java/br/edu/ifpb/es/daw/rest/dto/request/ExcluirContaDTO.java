package br.edu.ifpb.es.daw.rest.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ExcluirContaDTO {

    @NotBlank
    @Schema(description = "Senha atual, para confirmar a exclusão.", example = "senha123")
    private String senha;
}
