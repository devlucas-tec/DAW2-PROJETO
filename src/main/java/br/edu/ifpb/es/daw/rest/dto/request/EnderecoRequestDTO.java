package br.edu.ifpb.es.daw.rest.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EnderecoRequestDTO {
    @NotBlank private String rua;
    @NotBlank private String numero;
    private String complemento;
    @NotBlank private String cep;
    @NotBlank private String cidade;
    @NotBlank private String estado;
    private Long idCliente;
}