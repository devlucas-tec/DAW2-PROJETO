package br.edu.ifpb.es.daw.rest;

import br.edu.ifpb.es.daw.model.Notificacao;
import br.edu.ifpb.es.daw.rest.dto.response.ContagemNotificacoesDTO;
import br.edu.ifpb.es.daw.rest.dto.response.NotificacaoResponseDTO;
import br.edu.ifpb.es.daw.service.NotificacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notificacoes")
@PreAuthorize("isAuthenticated()")
@Tag(name = "Notificações", description = "Avisos do usuário logado (novo pedido, mudança de status, devolução)")
public class NotificacaoRestController {

    @Autowired private NotificacaoService service;

    @GetMapping
    @Operation(summary = "Notificações do usuário logado, mais recentes primeiro (10 por página)")
    public ResponseEntity<Page<NotificacaoResponseDTO>> listar(
            @RequestParam(defaultValue = "false") boolean somenteNaoLidas,
            @RequestParam(defaultValue = "0") int page) {
        return ResponseEntity.ok(service.minhas(somenteNaoLidas, page).map(NotificacaoRestController::from));
    }

    @GetMapping("/nao-lidas/contagem")
    @Operation(summary = "Quantidade de notificações não lidas (badge do sino; pode ser consultada por polling)")
    public ResponseEntity<ContagemNotificacoesDTO> contagem() {
        return ResponseEntity.ok(new ContagemNotificacoesDTO(service.contarNaoLidas()));
    }

    @PatchMapping("/{id}/lida")
    @Operation(summary = "Marcar uma notificação como lida")
    public ResponseEntity<NotificacaoResponseDTO> marcarComoLida(@PathVariable Long id) {
        return ResponseEntity.ok(from(service.marcarComoLida(id)));
    }

    @PatchMapping("/lidas")
    @Operation(summary = "Marcar todas as notificações como lidas")
    public ResponseEntity<ContagemNotificacoesDTO> marcarTodasComoLidas() {
        service.marcarTodasComoLidas();
        return ResponseEntity.ok(new ContagemNotificacoesDTO(0));
    }

    private static NotificacaoResponseDTO from(Notificacao n) {
        return new NotificacaoResponseDTO(n.getId(), n.getTitulo(), n.getMensagem(), n.getLink(),
                n.getIdPedido(), n.isLida(), n.getDataCriacao());
    }
}
