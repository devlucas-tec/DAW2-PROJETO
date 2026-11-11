package br.edu.ifpb.es.daw.rest.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class QuantidadeDTO {

    @NotNull
    @Min(1)
    @Schema(example = "2")
    private Integer quantidade;
}
