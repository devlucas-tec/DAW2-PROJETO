package br.edu.ifpb.es.daw.rest.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
public class VendedorRequestDTO {

    @NotBlank
    @Size(max = 100)
    @Schema(example = "Carlos Andrade")
    private String nome;

    @NotBlank
    @Size(max = 150)
    @Schema(example = "Tech Store Ltda")
    private String razaoSocial;

    @NotBlank
    @Size(max = 18)
    @Schema(example = "12.345.678/0001-99")
    private String cnpjCpf;

    @NotBlank
    @Email
    @Size(max = 100)
    @Schema(example = "techstore@email.com")
    private String email;

    @NotBlank
    @Size(min = 6)
    @Schema(example = "senha123")
    private String senha;

    @Size(max = 500)
    @Schema(description = "Descrição breve da loja, exibida no perfil público.",
            example = "Eletrônicos originais com nota fiscal e entrega para todo o Brasil.")
    private String descricao;

    @Size(max = 500)
    @URL(regexp = "^https?://.+", message = "deve ser uma URL http(s) válida")
    @Schema(description = "Imagem de capa da loja (opcional). Envie \"\" para remover.",
            example = "https://loremflickr.com/1200/300/store?lock=1")
    private String bannerUrl;
}
