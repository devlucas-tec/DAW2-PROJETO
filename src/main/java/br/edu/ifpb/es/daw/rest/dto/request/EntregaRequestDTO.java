package br.edu.ifpb.es.daw.rest.dto.request;

import br.edu.ifpb.es.daw.model.enums.StatusEntrega;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EntregaRequestDTO {

    @Schema(example = "Correios")
    private String transportadora;

    @Schema(example = "BR123456789BR")
    private String codigoRastreamento;

    @NotNull
    @Schema(example = "EMBALADO")
    private StatusEntrega statusEntrega;

    @NotNull
    @Schema(example = "1")
    private Long idPedido;
}