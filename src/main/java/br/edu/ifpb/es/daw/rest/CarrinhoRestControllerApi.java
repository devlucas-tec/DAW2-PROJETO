package br.edu.ifpb.es.daw.rest;

import br.edu.ifpb.es.daw.rest.dto.request.CarrinhoRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.CarrinhoResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "carrinhos", description = "API Carrinhos")
public interface CarrinhoRestControllerApi {
    @Operation(summary = "Criar carrinho") ResponseEntity<CarrinhoResponseDTO> adicionar(CarrinhoRequestDTO dto);
    @Operation(summary = "Buscar carrinho por ID") ResponseEntity<CarrinhoResponseDTO> recuperarPor(Long id);
    @Operation(summary = "Limpar carrinho") ResponseEntity<CarrinhoResponseDTO> limpar(Long id);
}