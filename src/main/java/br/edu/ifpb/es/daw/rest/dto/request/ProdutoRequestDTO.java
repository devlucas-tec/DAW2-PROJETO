package br.edu.ifpb.es.daw.rest.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProdutoRequestDTO {
    @NotBlank private String nome;
    private String descricao;
    @NotNull private BigDecimal preco;
    @NotNull private Integer estoque;
    @NotNull private Long idCategoria;
    @NotNull private Long idVendedor;
}