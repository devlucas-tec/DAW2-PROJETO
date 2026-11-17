package br.edu.ifpb.es.daw.rest.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LojaStatusDTO {

    @NotNull
    @Schema(description = "false fecha a loja temporariamente (os produtos saem da vitrine); true reabre.", example = "false")
    private Boolean aberta;
}
