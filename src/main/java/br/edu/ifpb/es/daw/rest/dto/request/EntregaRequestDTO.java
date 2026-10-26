package br.edu.ifpb.es.daw.rest.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EntregaRequestDTO {

    @Size(max = 50)
    @Schema(example = "Correios")
    private String transportadora;

    @Size(max = 50)
    @Schema(example = "BR123456789BR")
    private String codigoRastreamento;

    @NotNull
    @Schema(example = "1")
    private Long idPedido;
}
