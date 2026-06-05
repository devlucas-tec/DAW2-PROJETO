package br.edu.ifpb.es.daw.rest.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ClienteRequestDTO {
    @NotBlank private String nome;
    @NotBlank @Email private String email;
    @NotBlank private String senha;
    private String telefone;
}