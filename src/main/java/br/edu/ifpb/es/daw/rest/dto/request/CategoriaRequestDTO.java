package br.edu.ifpb.es.daw.rest.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoriaRequestDTO {

    @NotBlank
    @Size(max = 50)
    @Schema(example = "Eletrônicos")
    private String nome;

    @Size(max = 350)
    @Schema(example = "Produtos eletrônicos em geral")
    private String descricao;
}
