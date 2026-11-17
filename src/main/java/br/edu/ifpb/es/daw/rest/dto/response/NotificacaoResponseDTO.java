package br.edu.ifpb.es.daw.rest.dto.response;

import java.time.LocalDateTime;

public record NotificacaoResponseDTO(
        Long id,
        String titulo,
        String mensagem,
        String link,
        Long idPedido,
        boolean lida,
        LocalDateTime dataCriacao
) {}
