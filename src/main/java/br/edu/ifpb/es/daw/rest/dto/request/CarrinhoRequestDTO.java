package br.edu.ifpb.es.daw.rest.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CarrinhoRequestDTO {
    @NotNull private Long idCliente;
}