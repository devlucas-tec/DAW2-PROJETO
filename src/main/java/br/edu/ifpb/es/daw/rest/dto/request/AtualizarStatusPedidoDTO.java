package br.edu.ifpb.es.daw.rest.dto.request;

import br.edu.ifpb.es.daw.model.enums.EtapaPedido;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AtualizarStatusPedidoDTO {

    @NotNull
    @Schema(description = "PAGAMENTO_APROVADO, EMBALADO, EM_TRANSITO, SAIU_PARA_ENTREGA ou ENTREGUE (sempre a próxima etapa).",
            example = "EM_TRANSITO")
    private EtapaPedido status;

    @Size(max = 50)
    @Schema(description = "Obrigatório (aqui ou no EMBALADO) para EM_TRANSITO.", example = "Correios")
    private String transportadora;

    @Size(max = 50)
    @Schema(description = "Obrigatório (aqui ou no EMBALADO) para EM_TRANSITO.", example = "BR123456789BR")
    private String codigoRastreamento;
}
