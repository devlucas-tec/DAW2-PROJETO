package br.edu.ifpb.es.daw.rest.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VendedorResponseDTO {
    private Long id;
    private String nome;
    private String razaoSocial;
    private String cnpjCpf;
    private String email;
    private boolean ativo;
    private LocalDateTime dataCadastro;
}
