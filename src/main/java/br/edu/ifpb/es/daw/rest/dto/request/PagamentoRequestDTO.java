package br.edu.ifpb.es.daw.rest.dto.request;

import br.edu.ifpb.es.daw.model.enums.MetodoPagamento;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class PagamentoRequestDTO {

    @NotNull
    @Schema(example = "PIX")
    private MetodoPagamento metodo;

    @NotNull
    @Schema(example = "1299.90")
    private BigDecimal valorPago;

    @NotNull
    @Schema(example = "1")
    private Long idPedido;
}