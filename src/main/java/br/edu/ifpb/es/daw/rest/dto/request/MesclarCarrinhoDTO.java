package br.edu.ifpb.es.daw.rest.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/** Carrinho de visitante (localStorage) enviado depois do login. */
@Data
public class MesclarCarrinhoDTO {

    @NotNull
    @Size(max = 50)
    @Schema(description = "Itens do carrinho de visitante (até 50).")
    private List<@Valid @NotNull ItemMesclarCarrinhoDTO> itens;
}
