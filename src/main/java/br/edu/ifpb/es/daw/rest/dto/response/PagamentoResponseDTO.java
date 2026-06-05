package br.edu.ifpb.es.daw.rest.dto.response;

import br.edu.ifpb.es.daw.model.enums.MetodoPagamento;
import br.edu.ifpb.es.daw.model.enums.StatusPagamento;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PagamentoResponseDTO {
    private Long id;
    private MetodoPagamento metodo;
    private StatusPagamento status;
    private BigDecimal valorPago;
    private LocalDateTime dataPagamento;
    private Long idPedido;
}