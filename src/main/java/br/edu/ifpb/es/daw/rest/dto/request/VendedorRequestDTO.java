package br.edu.ifpb.es.daw.rest.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VendedorRequestDTO {
    @NotBlank private String razaoSocial;
    @NotBlank private String cnpjCpf;
}