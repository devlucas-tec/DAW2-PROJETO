package br.edu.ifpb.es.daw.rest.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AvaliacaoResponseDTO {
    private Long id;
    private Integer nota;
    private String comentario;
    private LocalDateTime dataAvaliacao;
    private Long idCliente;
    private String nomeCliente;
    private Long idProduto;
    private String nomeProduto;
}