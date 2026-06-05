package br.edu.ifpb.es.daw.rest.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoriaRequestDTO {
    @NotBlank private String nome;
    private String descricao;
}