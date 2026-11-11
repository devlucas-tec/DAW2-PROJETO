package br.edu.ifpb.es.daw.rest.dto.response;

import br.edu.ifpb.es.daw.model.enums.MetodoPagamento;
import br.edu.ifpb.es.daw.model.enums.StatusPagamento;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/** Resumo de uma compra (checkout): um pedido por vendedor. */
@Data
public class CompraResponseDTO {
    private Long id;
    private String codigo;
    private LocalDateTime dataCompra;
    private Long idCliente;
    private String nomeCliente;
    private String codigoCupom;
    private MetodoPagamento metodoPagamento;
    private StatusPagamento statusPagamento;
    private BigDecimal valorSubtotal;
    private BigDecimal valorDesconto;
    private BigDecimal valorTotal;
    private int quantidadePedidos;
    private List<PedidoResponseDTO> pedidos;
}
