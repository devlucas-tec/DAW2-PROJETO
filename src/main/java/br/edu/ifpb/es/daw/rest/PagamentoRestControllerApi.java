package br.edu.ifpb.es.daw.rest;

import br.edu.ifpb.es.daw.rest.dto.request.PagamentoRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.PagamentoResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "pagamentos", description = "API Pagamentos")
public interface PagamentoRestControllerApi {
    @Operation(summary = "Registrar pagamento") ResponseEntity<PagamentoResponseDTO> adicionar(PagamentoRequestDTO dto);
    @Operation(summary = "Buscar pagamento por ID") ResponseEntity<PagamentoResponseDTO> recuperarPor(Long id);
    @Operation(summary = "Confirmar pagamento") ResponseEntity<PagamentoResponseDTO> confirmar(Long id);
    @Operation(summary = "Cancelar pagamento") ResponseEntity<PagamentoResponseDTO> cancelar(Long id);
}