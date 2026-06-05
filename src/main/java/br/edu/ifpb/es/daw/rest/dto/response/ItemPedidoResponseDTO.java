package br.edu.ifpb.es.daw.rest.dto.response;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ItemPedidoResponseDTO {
    private Long idPedido;
    private Long idProduto;
    private String nomeProduto;
    private Integer quantidade;
    private BigDecimal precoUnitario;
}