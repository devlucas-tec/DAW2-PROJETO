package br.edu.ifpb.es.daw.rest.dto.response;

import br.edu.ifpb.es.daw.model.enums.StatusEntrega;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class EntregaResponseDTO {
    private Long id;
    private String transportadora;
    private String codigoRastreamento;
    private StatusEntrega statusEntrega;
    private LocalDateTime dataEnvio;
    private LocalDateTime dataEntregaPrevista;
    private Long idPedido;
}