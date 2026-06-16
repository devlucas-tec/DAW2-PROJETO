package br.edu.ifpb.es.daw.rest;

import br.edu.ifpb.es.daw.rest.dto.request.VendedorRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.VendedorResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Vendedores", description = "API Vendedores")
public interface VendedorRestControllerApi {
    @Operation(summary = "Listar todos os vendedores")
    ResponseEntity<Page<VendedorResponseDTO>> listar(@RequestParam(defaultValue = "0") int page);

    @Operation(summary = "Criar vendedor")
    ResponseEntity<VendedorResponseDTO> adicionar(@RequestBody VendedorRequestDTO dto);

    @Operation(summary = "Buscar vendedor por ID")
    ResponseEntity<VendedorResponseDTO> recuperarPor(@PathVariable Long id);

    @Operation(summary = "Atualizar vendedor")
    ResponseEntity<VendedorResponseDTO> atualizar(@PathVariable Long id, @RequestBody VendedorRequestDTO dto);

    @Operation(summary = "Remover vendedor")
    ResponseEntity<Void> remover(@PathVariable Long id);
}