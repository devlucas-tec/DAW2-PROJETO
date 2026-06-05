package br.edu.ifpb.es.daw.rest.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CupomRequestDTO {
    @NotBlank private String codigo;
    @NotNull private BigDecimal valorDesconto;
    private LocalDate dataExpiracao;
}