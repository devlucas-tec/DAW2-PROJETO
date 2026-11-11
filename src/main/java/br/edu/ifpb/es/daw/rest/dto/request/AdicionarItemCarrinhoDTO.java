package br.edu.ifpb.es.daw.rest.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AdicionarItemCarrinhoDTO {

    @NotNull
    @Schema(example = "1")
    private Long idProduto;

    @NotNull
    @Min(1)
    @Schema(description = "Somada à quantidade que já estiver no carrinho.", example = "1")
    private Integer quantidade;
}
