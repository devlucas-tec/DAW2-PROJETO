package br.edu.ifpb.es.daw.rest.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DevolucaoRequestDTO {
    @NotBlank private String motivo;
    @NotNull private Long idPedido;
}