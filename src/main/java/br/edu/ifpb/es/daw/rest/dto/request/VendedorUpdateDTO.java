package br.edu.ifpb.es.daw.rest.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class VendedorUpdateDTO {

    @Size(min = 1, max = 100)
    @Schema(example = "Carlos Andrade")
    private String nome;

    @Size(min = 1, max = 150)
    @Schema(example = "Tech Store Ltda")
    private String razaoSocial;

    @Size(min = 1, max = 18)
    @Schema(example = "12.345.678/0001-99")
    private String cnpjCpf;

    @Email
    @Size(max = 100)
    @Schema(example = "techstore@email.com")
    private String email;

    @Size(min = 6)
    @Schema(example = "novaSenha123")
    private String senha;
}
