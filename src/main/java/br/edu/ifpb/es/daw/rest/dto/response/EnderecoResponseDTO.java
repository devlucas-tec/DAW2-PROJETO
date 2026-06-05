package br.edu.ifpb.es.daw.rest.dto.response;

import lombok.Data;

@Data
public class EnderecoResponseDTO {
    private Long id;
    private String rua;
    private String numero;
    private String complemento;
    private String cep;
    private String cidade;
    private String estado;
    private Long idCliente;
}