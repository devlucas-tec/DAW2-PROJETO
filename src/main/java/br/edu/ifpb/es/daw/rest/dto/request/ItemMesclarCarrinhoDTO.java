package br.edu.ifpb.es.daw.rest.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/** Item do carrinho de visitante (localStorage) enviado para ser mesclado após o login. */
@Data
public class ItemMesclarCarrinhoDTO {

    @NotNull
    @Schema(example = "1")
    private Long idProduto;

    @NotNull
    @Min(1)
    @Schema(example = "2")
    private Integer quantidade;
}
