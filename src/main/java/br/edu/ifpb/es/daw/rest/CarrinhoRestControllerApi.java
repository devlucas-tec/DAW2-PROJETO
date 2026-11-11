package br.edu.ifpb.es.daw.rest;

import br.edu.ifpb.es.daw.rest.dto.request.AdicionarItemCarrinhoDTO;
import br.edu.ifpb.es.daw.rest.dto.request.CarrinhoRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.request.QuantidadeDTO;
import br.edu.ifpb.es.daw.rest.dto.response.CarrinhoResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Carrinho", description = "API Carrinho")
public interface CarrinhoRestControllerApi {

    @Operation(summary = "Carrinho do cliente logado (criado automaticamente se não existir)")
    ResponseEntity<CarrinhoResponseDTO> meu();

    @Operation(summary = "Adicionar produto ao carrinho do cliente logado (soma à quantidade existente)")
    ResponseEntity<CarrinhoResponseDTO> adicionarItem(AdicionarItemCarrinhoDTO dto);

    @Operation(summary = "Definir a quantidade de um produto no carrinho do cliente logado (adiciona se não existir)")
    ResponseEntity<CarrinhoResponseDTO> definirQuantidade(Long idProduto, QuantidadeDTO dto);

    @Operation(summary = "Remover um produto do carrinho do cliente logado")
    ResponseEntity<CarrinhoResponseDTO> removerItem(Long idProduto);

    @Operation(summary = "Esvaziar o carrinho do cliente logado")
    ResponseEntity<CarrinhoResponseDTO> esvaziar();

    @Operation(summary = "Criar carrinho")
    ResponseEntity<CarrinhoResponseDTO> adicionar(CarrinhoRequestDTO dto);

    @Operation(summary = "Buscar carrinho por ID")
    ResponseEntity<CarrinhoResponseDTO> recuperarPor(Long id);

    @Operation(summary = "Limpar carrinho")
    ResponseEntity<CarrinhoResponseDTO> limpar(Long id);
}
