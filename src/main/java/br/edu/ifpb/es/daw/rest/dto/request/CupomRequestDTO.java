package br.edu.ifpb.es.daw.rest.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CupomRequestDTO {

    @NotBlank
    @Schema(example = "DESCONTO10")
    private String codigo;

    @NotNull
    @Schema(example = "10.00")
    private BigDecimal valorDesconto;

    @Schema(example = "2026-12-31")
    private LocalDate dataExpiracao;
}