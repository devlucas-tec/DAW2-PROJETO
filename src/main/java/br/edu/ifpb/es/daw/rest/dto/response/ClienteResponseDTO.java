package br.edu.ifpb.es.daw.rest.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ClienteResponseDTO {
    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private LocalDateTime dataCadastro;
}