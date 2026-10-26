package br.edu.ifpb.es.daw.rest.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EnderecoRequestDTO {

    @NotBlank
    @Size(max = 100)
    @Schema(example = "Rua das Flores")
    private String rua;

    @NotBlank
    @Size(max = 10)
    @Schema(example = "123")
    private String numero;

    @Size(max = 50)
    @Schema(example = "Apto 45")
    private String complemento;

    @NotBlank
    @Pattern(regexp = "\\d{5}-?\\d{3}", message = "deve estar no formato 00000-000")
    @Schema(example = "58400-000")
    private String cep;

    @NotBlank
    @Size(max = 50)
    @Schema(example = "Campina Grande")
    private String cidade;

    @NotBlank
    @Pattern(regexp = "[A-Za-z]{2}", message = "deve ser a sigla do estado com 2 letras")
    @Schema(example = "PB")
    private String estado;

    @Schema(description = "Obrigatório apenas para ADMIN. Para CLIENTE é usado o próprio usuário logado.", example = "1")
    private Long idCliente;
}
