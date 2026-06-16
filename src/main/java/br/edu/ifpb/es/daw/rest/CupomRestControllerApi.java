package br.edu.ifpb.es.daw.rest;

import br.edu.ifpb.es.daw.model.enums.StatusCupom;
import br.edu.ifpb.es.daw.rest.dto.request.CupomRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.CupomResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Cupons", description = "API Cupons")
public interface CupomRestControllerApi {
    @Operation(summary = "Listar todos os cupons")
    ResponseEntity<Page<CupomResponseDTO>> listar(@RequestParam(defaultValue = "0") int page);

    @Operation(summary = "Buscar cupons por código e/ou status")
    ResponseEntity<Page<CupomResponseDTO>> buscar(
            @RequestParam(required = false) String codigo,
            @RequestParam(required = false) StatusCupom status,
            @RequestParam(defaultValue = "0") int page
    );

    @Operation(summary = "Criar cupom")
    ResponseEntity<CupomResponseDTO> adicionar(@RequestBody CupomRequestDTO dto);

    @Operation(summary = "Buscar cupom por ID")
    ResponseEntity<CupomResponseDTO> recuperarPor(@PathVariable Long id);

    @Operation(summary = "Atualizar cupom")
    ResponseEntity<CupomResponseDTO> atualizar(@PathVariable Long id, @RequestBody CupomRequestDTO dto);

    @Operation(summary = "Remover cupom")
    ResponseEntity<Void> remover(@PathVariable Long id);
}