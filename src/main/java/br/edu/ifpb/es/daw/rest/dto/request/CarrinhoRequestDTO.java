package br.edu.ifpb.es.daw.rest.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CarrinhoRequestDTO {

    @NotNull
    @Schema(example = "1")
    private Long idCliente;
}