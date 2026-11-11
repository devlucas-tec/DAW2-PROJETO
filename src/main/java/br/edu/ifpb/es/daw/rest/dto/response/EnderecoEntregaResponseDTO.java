package br.edu.ifpb.es.daw.rest.dto.response;

import lombok.Data;

@Data
public class EnderecoEntregaResponseDTO {
    private String rua;
    private String numero;
    private String complemento;
    private String cep;
    private String cidade;
    private String estado;
}
