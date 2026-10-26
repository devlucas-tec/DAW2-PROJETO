package br.edu.ifpb.es.daw.rest.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ClienteUpdateDTO {

    @Size(min = 1, max = 100)
    @Schema(example = "Alex Pereira")
    private String nome;

    @Email
    @Size(max = 100)
    @Schema(example = "poatan@gmail.com")
    private String email;

    @Size(min = 6)
    @Schema(example = "novaSenha123")
    private String senha;

    @Size(max = 20)
    @Schema(example = "83999990001")
    private String telefone;
}
