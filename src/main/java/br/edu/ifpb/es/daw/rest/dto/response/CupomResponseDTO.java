package br.edu.ifpb.es.daw.rest.dto.response;

import br.edu.ifpb.es.daw.model.enums.StatusCupom;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CupomResponseDTO {
    private Long id;
    private String codigo;
    private BigDecimal valorDesconto;
    private LocalDate dataExpiracao;
    private StatusCupom status;
}