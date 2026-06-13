package br.edu.ifpb.es.daw.rest.dto.response;

import lombok.Data;

@Data
public class VendedorResponseDTO {
    private Long id;
    private String razaoSocial;
    private String cnpjCpf;
    private String email;
}