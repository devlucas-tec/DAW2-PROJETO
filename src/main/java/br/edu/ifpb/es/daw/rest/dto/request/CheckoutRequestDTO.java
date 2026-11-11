package br.edu.ifpb.es.daw.rest.dto.request;

import br.edu.ifpb.es.daw.model.enums.MetodoPagamento;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CheckoutRequestDTO {

    @NotNull
    @Schema(description = "Endereço de entrega (precisa ser do cliente logado).", example = "1")
    private Long idEndereco;

    @Size(max = 20)
    @Schema(description = "Opcional.", example = "CUPOM01")
    private String codigoCupom;

    @NotNull
    @Schema(description = "PIX e BOLETO ficam PENDENTES; CARTAO (simulado) é APROVADO na hora.", example = "PIX")
    private MetodoPagamento metodoPagamento;
}
