package br.edu.ifpb.es.daw.rest.dto.response;

import br.edu.ifpb.es.daw.model.enums.StatusPedido;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PedidoResponseDTO {
    private Long id;
    private LocalDateTime dataPedido;
    private BigDecimal valorTotal;
    private StatusPedido status;
    private Long idCliente;
    private String nomeCliente;
}