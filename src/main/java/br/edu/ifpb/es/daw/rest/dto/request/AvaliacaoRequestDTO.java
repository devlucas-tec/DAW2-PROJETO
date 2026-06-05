package br.edu.ifpb.es.daw.rest.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AvaliacaoRequestDTO {
    @NotNull @Min(1) @Max(5) private Integer nota;
    private String comentario;
    @NotNull private Long idCliente;
    @NotNull private Long idProduto;
}