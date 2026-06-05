package br.edu.ifpb.es.daw.rest.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ItemCarrinhoRequestDTO {
    @NotNull private Long idCarrinho;
    @NotNull private Long idProduto;
    @NotNull @Min(0) private Integer quantidade;
    @NotNull private BigDecimal precoUnitario;
}