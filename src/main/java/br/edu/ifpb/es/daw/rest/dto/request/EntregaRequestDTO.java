package br.edu.ifpb.es.daw.rest.dto.request;

import br.edu.ifpb.es.daw.model.enums.StatusEntrega;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EntregaRequestDTO {
    private String transportadora;
    private String codigoRastreamento;
    @NotNull private StatusEntrega statusEntrega;
    @NotNull private Long idPedido;
}