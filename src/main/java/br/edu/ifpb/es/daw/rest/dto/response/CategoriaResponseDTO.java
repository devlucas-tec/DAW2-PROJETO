package br.edu.ifpb.es.daw.rest.dto.response;

import lombok.Data;

@Data
public class CategoriaResponseDTO {
    private Long id;
    private String nome;
    private String descricao;
}