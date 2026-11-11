package br.edu.ifpb.es.daw.rest.dto.response;

import br.edu.ifpb.es.daw.model.enums.EtapaPedido;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PedidoHistoricoResponseDTO {
    private Long id;
    private Long idPedido;
    private EtapaPedido etapa;
    private LocalDateTime data;
    private String descricao;
    private Long idAutor;
    private String nomeAutor;
    private String papelAutor;
}
