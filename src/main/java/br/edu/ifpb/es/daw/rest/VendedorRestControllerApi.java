package br.edu.ifpb.es.daw.rest;

import br.edu.ifpb.es.daw.rest.dto.request.VendedorRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.request.VendedorUpdateDTO;
import br.edu.ifpb.es.daw.rest.dto.response.VendedorPublicoResponseDTO;
import br.edu.ifpb.es.daw.rest.dto.response.VendedorResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

@Tag(name = "Vendedores", description = "API Vendedores")
public interface VendedorRestControllerApi {

    @Operation(summary = "Listar todos os vendedores com dados completos (ADMIN)")
    ResponseEntity<Page<VendedorResponseDTO>> listar(int page);

    @Operation(summary = "Cadastrar vendedor (público)")
    ResponseEntity<VendedorResponseDTO> adicionar(VendedorRequestDTO dto);

    @Operation(summary = "Dados completos do vendedor logado")
    ResponseEntity<VendedorResponseDTO> me();

    @Operation(summary = "Perfil público da loja (sem e-mail e CNPJ/CPF)")
    ResponseEntity<VendedorPublicoResponseDTO> recuperarPor(Long id);

    @Operation(summary = "Atualizar vendedor (apenas os campos enviados são alterados)")
    ResponseEntity<VendedorResponseDTO> atualizar(Long id, VendedorUpdateDTO dto);

    @Operation(summary = "Remover vendedor")
    ResponseEntity<Void> remover(Long id);
}
