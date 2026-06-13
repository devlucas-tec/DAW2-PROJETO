package br.edu.ifpb.es.daw.rest.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProdutoRequestDTO {

    @NotBlank
    @Schema(example = "Smartphone XYZ")
    private String nome;

    @Schema(example = "Smartphone com 128GB de armazenamento")
    private String descricao;

    @NotNull
    @Schema(example = "1299.90")
    private BigDecimal preco;

    @NotNull
    @Schema(example = "50")
    private Integer estoque;

    @NotNull
    @Schema(example = "1")
    private Long idCategoria;

    @NotNull
    @Schema(example = "1")
    private Long idVendedor;
}