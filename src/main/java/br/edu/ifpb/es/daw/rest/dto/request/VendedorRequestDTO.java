package br.edu.ifpb.es.daw.rest.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class VendedorRequestDTO {

    @NotBlank
    @Schema(example = "Carlos Andrade")
    private String nome;

    @NotBlank
    @Schema(example = "Tech Store Ltda")
    private String razaoSocial;

    @NotBlank
    @Schema(example = "12.345.678/0001-99")
    private String cnpjCpf;

    @NotBlank @Email
    @Schema(example = "techstore@email.com")
    private String email;

    @NotBlank @Size(min = 6)
    @Schema(example = "senha123")
    private String senha;
}