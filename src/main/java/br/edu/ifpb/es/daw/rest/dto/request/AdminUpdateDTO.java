package br.edu.ifpb.es.daw.rest.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AdminUpdateDTO {

    @Size(min = 1, max = 100)
    @Schema(example = "Admin")
    private String nome;

    @Email
    @Size(max = 100)
    @Schema(example = "admin@gmail.com")
    private String email;

    @Size(min = 6)
    @Schema(example = "senha123")
    private String senha;
}
