package br.edu.ifpb.es.daw.rest.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoriaRequestDTO {

    @NotBlank
    @Schema(example = "Eletrônicos")
    private String nome;

    @Schema(example = "Produtos eletrônicos em geral")
    private String descricao;
}