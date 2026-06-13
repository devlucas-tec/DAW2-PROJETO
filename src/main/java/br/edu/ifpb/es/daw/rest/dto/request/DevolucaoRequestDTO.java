package br.edu.ifpb.es.daw.rest.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DevolucaoRequestDTO {

    @NotBlank
    @Schema(example = "Produto chegou com defeito")
    private String motivo;

    @NotNull
    @Schema(example = "1")
    private Long idPedido;
}