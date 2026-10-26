package br.edu.ifpb.es.daw.rest.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DevolucaoRequestDTO {

    @NotBlank
    @Size(max = 1000)
    @Schema(example = "Produto chegou com defeito")
    private String motivo;

    @NotNull
    @Schema(example = "1")
    private Long idPedido;
}
