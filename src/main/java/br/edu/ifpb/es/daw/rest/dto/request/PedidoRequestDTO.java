package br.edu.ifpb.es.daw.rest.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class PedidoRequestDTO {
    @NotNull private BigDecimal valorTotal;
    @NotNull private Long idCliente;
    private Long idCupom;
}