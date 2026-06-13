package br.edu.ifpb.es.daw.rest.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AdminRequestDTO {
    @NotBlank @Email private String email;
    @NotBlank @Size(min = 6) private String senha;
}