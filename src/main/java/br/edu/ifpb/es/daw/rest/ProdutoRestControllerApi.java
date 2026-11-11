package br.edu.ifpb.es.daw.rest;

import br.edu.ifpb.es.daw.rest.dto.request.ProdutoRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.request.ProdutoUpdateDTO;
import br.edu.ifpb.es.daw.rest.dto.response.ProdutoResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

@Tag(name = "Produtos", description = "API Produtos")
public interface ProdutoRestControllerApi {

    @Operation(summary = "Listar a vitrine (público): produtos ativos e com estoque, mais recentes primeiro")
    ResponseEntity<Page<ProdutoResponseDTO>> listar(int page, Integer size);

    @Operation(summary = "Buscar na vitrine (público) por nome, categoria, vendedor e/ou faixa de preço, com ordenação")
    ResponseEntity<Page<ProdutoResponseDTO>> buscar(
            String nome,
            Long idCategoria,
            Long idVendedor,
            BigDecimal precoMin,
            BigDecimal precoMax,
            @Parameter(description = "Padrão true: esconde produtos sem estoque.") Boolean somenteComEstoque,
            @Parameter(description = "preco,asc | preco,desc | nome,asc | nome,desc | dataCadastro,desc | dataCadastro,asc | avaliacao,desc") String sort,
            int page,
            @Parameter(description = "Tamanho da página (1 a 50, padrão 10).") Integer size
    );

    @Operation(summary = "Listar os produtos do vendedor logado, inclusive inativos e sem estoque")
    ResponseEntity<Page<ProdutoResponseDTO>> meus(String nome, Boolean ativo, int page, Integer size);

    @Operation(summary = "Criar produto")
    ResponseEntity<ProdutoResponseDTO> adicionar(ProdutoRequestDTO dto);

    @Operation(summary = "Buscar produto por ID (público; anúncios pausados só para o dono e o ADMIN)")
    ResponseEntity<ProdutoResponseDTO> recuperarPor(Long id);

    @Operation(summary = "Atualizar produto parcialmente (inclui pausar/reativar com 'ativo' e trocar 'imagemUrl')")
    ResponseEntity<ProdutoResponseDTO> atualizar(Long id, ProdutoUpdateDTO dto);

    @Operation(summary = "Remover produto")
    ResponseEntity<Void> remover(Long id);
}
