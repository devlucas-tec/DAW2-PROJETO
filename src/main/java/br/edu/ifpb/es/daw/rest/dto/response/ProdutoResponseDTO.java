package br.edu.ifpb.es.daw.rest.dto.response;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProdutoResponseDTO {
    private Long id;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private Integer estoque;
    private String nomeCategoria;
    private String nomeVendedor;
}