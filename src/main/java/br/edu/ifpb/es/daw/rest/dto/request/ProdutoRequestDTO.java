package br.edu.ifpb.es.daw.rest.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;
import java.util.List;

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

    @NotBlank
    @Size(max = 500)
    @URL(regexp = "^https?://.+", message = "deve ser uma URL http(s) válida")
    @Schema(example = "https://loremflickr.com/640/640/smartphone?lock=1")
    private String imagemUrl;

    @Size(max = 10)
    @Schema(description = "Galeria opcional de imagens adicionais.")
    private List<@NotBlank @Size(max = 500) @URL(regexp = "^https?://.+", message = "deve ser uma URL http(s) válida") String> imagens;

    @Schema(description = "Padrão true. Use false para cadastrar o anúncio pausado.", example = "true")
    private Boolean ativo;

    @NotNull
    @Schema(example = "1")
    private Long idCategoria;

    @Schema(description = "Obrigatório apenas para ADMIN. Para VENDEDOR é sempre usado o usuário logado (o valor enviado é ignorado).", example = "1")
    private Long idVendedor;
}
