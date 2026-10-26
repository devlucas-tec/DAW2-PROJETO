package br.edu.ifpb.es.daw.rest.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProdutoRequestDTO {

    @NotBlank
    @Size(max = 150)
    @Schema(example = "Smartphone XYZ")
    private String nome;

    @Size(max = 500)
    @Schema(example = "Smartphone com 128GB de armazenamento")
    private String descricao;

    @NotNull
    @Positive
    @Schema(example = "1299.90")
    private BigDecimal preco;

    @NotNull
    @PositiveOrZero
    @Schema(example = "50")
    private Integer estoque;

    @NotNull
    @Schema(example = "1")
    private Long idCategoria;

    @Schema(description = "Obrigatório apenas para ADMIN. Para VENDEDOR é usado o próprio usuário logado.", example = "1")
    private Long idVendedor;
}
