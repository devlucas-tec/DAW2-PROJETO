package br.edu.ifpb.es.daw.rest.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class PedidoRequestDTO {

    @Schema(description = "Obrigatório apenas para ADMIN. Para CLIENTE é usado o próprio usuário logado.", example = "1")
    private Long idCliente;

    @Schema(example = "1")
    private Long idCupom;
}
