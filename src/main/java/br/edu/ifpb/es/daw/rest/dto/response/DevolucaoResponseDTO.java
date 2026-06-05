package br.edu.ifpb.es.daw.rest.dto.response;

import br.edu.ifpb.es.daw.model.enums.StatusDevolucao;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DevolucaoResponseDTO {
    private Long id;
    private LocalDateTime dataDevolucao;
    private String motivo;
    private StatusDevolucao status;
    private Long idPedido;
}