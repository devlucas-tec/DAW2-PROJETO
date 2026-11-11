package br.edu.ifpb.es.daw.rest;

import br.edu.ifpb.es.daw.rest.dto.request.AvaliacaoRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.AvaliacaoResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

@Tag(name = "Avaliações", description = "API Avaliações")
public interface AvaliacaoRestControllerApi {

    @Operation(summary = "Listar avaliações de um produto (público, mais recentes primeiro). Sem idProduto, lista todas (ADMIN)")
    ResponseEntity<Page<AvaliacaoResponseDTO>> listar(Long idProduto, int page);

    @Operation(summary = "Listar as avaliações do cliente logado")
    ResponseEntity<Page<AvaliacaoResponseDTO>> minhas(int page);

    @Operation(summary = "Criar avaliação (só para produtos de pedidos ENTREGUES)")
    ResponseEntity<AvaliacaoResponseDTO> adicionar(AvaliacaoRequestDTO dto);

    @Operation(summary = "Buscar avaliação por ID")
    ResponseEntity<AvaliacaoResponseDTO> recuperarPor(Long id);

    @Operation(summary = "Remover avaliação (autor ou ADMIN, para moderação)")
    ResponseEntity<Void> remover(Long id);
}
