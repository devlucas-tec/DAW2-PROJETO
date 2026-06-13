package br.edu.ifpb.es.daw.rest.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class VendedorRequestDTO {
    @NotBlank private String nome;
    @NotBlank private String razaoSocial;
    @NotBlank private String cnpjCpf;

    @NotBlank @Email private String email;
    @NotBlank @Size(min = 6) private String senha;
}