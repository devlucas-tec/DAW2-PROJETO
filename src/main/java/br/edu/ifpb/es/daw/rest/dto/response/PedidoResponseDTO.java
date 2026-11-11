package br.edu.ifpb.es.daw.rest.dto.response;

import br.edu.ifpb.es.daw.model.enums.EtapaPedido;
import br.edu.ifpb.es.daw.model.enums.MetodoPagamento;
import br.edu.ifpb.es.daw.model.enums.StatusEntrega;
import br.edu.ifpb.es.daw.model.enums.StatusPagamento;
import br.edu.ifpb.es.daw.model.enums.StatusPedido;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PedidoResponseDTO {
    private Long id;
    private LocalDateTime dataPedido;
    private BigDecimal subtotal;
    private BigDecimal valorDesconto;
    private BigDecimal valorTotal;
    private StatusPedido status;
    /** Etapa atual para a timeline do cliente (ex.: EM_TRANSITO). */
    private EtapaPedido etapaAtual;
    /** Próxima etapa que o vendedor pode registrar; null quando não há transição possível. */
    private EtapaPedido proximaEtapa;
    private Long idCliente;
    private String nomeCliente;
    private String codigoCompra;
    private Long idCupom;
    private String codigoCupom;
    private EnderecoEntregaResponseDTO enderecoEntrega;
    private Long idPagamento;
    private MetodoPagamento metodoPagamento;
    private StatusPagamento statusPagamento;
    private Long idEntrega;
    private StatusEntrega statusEntrega;
    private String transportadora;
    private String codigoRastreamento;
    private LocalDateTime dataEnvio;
    private LocalDateTime dataEntregaPrevista;
    private LocalDateTime dataEntrega;
    private List<ItemPedidoResponseDTO> itens;
}
