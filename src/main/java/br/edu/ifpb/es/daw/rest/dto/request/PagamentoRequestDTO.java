package br.edu.ifpb.es.daw.rest.dto.request;

import br.edu.ifpb.es.daw.model.enums.MetodoPagamento;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class PagamentoRequestDTO {
    @NotNull private MetodoPagamento metodo;
    @NotNull private BigDecimal valorPago;
    @NotNull private Long idPedido;
}