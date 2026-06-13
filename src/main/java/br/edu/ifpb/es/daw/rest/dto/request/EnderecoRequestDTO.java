package br.edu.ifpb.es.daw.rest.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EnderecoRequestDTO {

    @NotBlank
    @Schema(example = "Rua das Flores")
    private String rua;

    @NotBlank
    @Schema(example = "123")
    private String numero;

    @Schema(example = "Apto 45")
    private String complemento;

    @NotBlank
    @Schema(example = "58400-000")
    private String cep;

    @NotBlank
    @Schema(example = "Campina Grande")
    private String cidade;

    @NotBlank
    @Schema(example = "PB")
    private String estado;

    @Schema(example = "1")
    private Long idCliente;
}