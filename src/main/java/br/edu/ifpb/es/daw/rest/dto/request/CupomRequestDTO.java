package br.edu.ifpb.es.daw.rest.dto.request;

import br.edu.ifpb.es.daw.model.enums.StatusCupom;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CupomRequestDTO {

    @NotBlank
    @Size(max = 20)
    @Schema(example = "DESCONTO10")
    private String codigo;

    @NotNull
    @Positive
    @Schema(example = "10.00")
    private BigDecimal valorDesconto;

    @FutureOrPresent
    @Schema(example = "2026-12-31")
    private LocalDate dataExpiracao;

    @Schema(description = "Opcional. Quando omitido, o cupom é criado ATIVO ou mantém o status atual.", example = "ATIVO")
    private StatusCupom status;
}
