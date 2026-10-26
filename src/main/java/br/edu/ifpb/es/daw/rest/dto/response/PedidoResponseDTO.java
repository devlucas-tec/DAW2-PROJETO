package br.edu.ifpb.es.daw.rest.dto.response;

import br.edu.ifpb.es.daw.model.enums.StatusPedido;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PedidoResponseDTO {
    private Long id;
    private LocalDateTime dataPedido;
    private BigDecimal valorTotal;
    private StatusPedido status;
    private Long idCliente;
    private String nomeCliente;
    private Long idCupom;
    private String codigoCupom;
    private BigDecimal valorDesconto;
    private List<ItemPedidoResponseDTO> itens;
}
