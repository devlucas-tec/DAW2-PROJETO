package br.edu.ifpb.es.daw.rest.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class PedidoRequestDTO {

    @NotNull
    @Schema(example = "1299.90")
    private BigDecimal valorTotal;

    @NotNull
    @Schema(example = "1")
    private Long idCliente;

    @Schema(example = "1")
    private Long idCupom;
}