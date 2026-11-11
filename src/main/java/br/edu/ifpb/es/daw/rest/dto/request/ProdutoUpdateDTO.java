package br.edu.ifpb.es.daw.rest.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;
import java.util.List;

/** Atualização parcial: só os campos enviados são alterados. */
@Data
public class ProdutoUpdateDTO {

    @Size(min = 1, max = 150)
    @Schema(example = "Smartphone XYZ")
    private String nome;

    @Size(max = 500)
    @Schema(example = "Smartphone com 128GB de armazenamento")
    private String descricao;

    @Positive
    @Schema(example = "1299.90")
    private BigDecimal preco;

    @PositiveOrZero
    @Schema(example = "50")
    private Integer estoque;

    @Size(min = 1, max = 500)
    @URL(regexp = "^https?://.+", message = "deve ser uma URL http(s) válida")
    @Schema(example = "https://loremflickr.com/640/640/smartphone?lock=1")
    private String imagemUrl;

    @Size(max = 10)
    @Schema(description = "Substitui a galeria inteira. Envie [] para remover todas as imagens adicionais.")
    private List<@NotBlank @Size(max = 500) @URL(regexp = "^https?://.+", message = "deve ser uma URL http(s) válida") String> imagens;

    @Schema(description = "false pausa o anúncio (some da vitrine); true reativa.", example = "false")
    private Boolean ativo;

    @Schema(example = "1")
    private Long idCategoria;
}
