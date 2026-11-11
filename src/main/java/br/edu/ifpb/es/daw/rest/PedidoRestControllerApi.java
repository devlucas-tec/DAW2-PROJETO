package br.edu.ifpb.es.daw.rest;

import br.edu.ifpb.es.daw.model.enums.StatusPedido;
import br.edu.ifpb.es.daw.rest.dto.request.AtualizarStatusPedidoDTO;
import br.edu.ifpb.es.daw.rest.dto.request.PedidoRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.PedidoHistoricoResponseDTO;
import br.edu.ifpb.es.daw.rest.dto.response.PedidoResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Pedidos", description = "API Pedidos")
public interface PedidoRestControllerApi {

    @Operation(summary = "Listar pedidos do usuário logado (ADMIN vê todos), com filtro opcional por status")
    ResponseEntity<Page<PedidoResponseDTO>> listar(StatusPedido status, int page);

    @Operation(summary = "Criar pedido avulso (o fluxo principal é POST /checkout)")
    ResponseEntity<PedidoResponseDTO> adicionar(PedidoRequestDTO dto);

    @Operation(summary = "Buscar pedido por ID")
    ResponseEntity<PedidoResponseDTO> recuperarPor(Long id);

    @Operation(summary = "Timeline do pedido (histórico de etapas)")
    ResponseEntity<List<PedidoHistoricoResponseDTO>> historico(Long id);

    @Operation(summary = "Avançar o status do pedido (vendedor dono dos itens ou ADMIN): "
            + "PAGAMENTO_APROVADO → EMBALADO → EM_TRANSITO → SAIU_PARA_ENTREGA → ENTREGUE")
    ResponseEntity<PedidoResponseDTO> atualizarStatus(Long id, AtualizarStatusPedidoDTO dto);

    @Operation(summary = "Fechar pedido")
    ResponseEntity<PedidoResponseDTO> fechar(Long id);

    @Operation(summary = "Cancelar pedido")
    ResponseEntity<PedidoResponseDTO> cancelar(Long id);

    @Operation(summary = "Remover pedido")
    ResponseEntity<Void> remover(Long id);
}
