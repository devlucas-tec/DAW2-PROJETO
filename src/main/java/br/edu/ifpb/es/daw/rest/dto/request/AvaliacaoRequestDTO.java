package br.edu.ifpb.es.daw.rest.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AvaliacaoRequestDTO {

    @NotNull @Min(1) @Max(5)
    @Schema(example = "5")
    private Integer nota;

    @Schema(example = "Produto excelente, chegou rápido!")
    private String comentario;

    @NotNull
    @Schema(example = "1")
    private Long idCliente;

    @NotNull
    @Schema(example = "1")
    private Long idProduto;
}