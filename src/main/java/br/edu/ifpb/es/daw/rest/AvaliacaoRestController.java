package br.edu.ifpb.es.daw.rest;

import br.edu.ifpb.es.daw.exception.EntidadeNaoEncontradaException;
import br.edu.ifpb.es.daw.mapper.AvaliacaoMapper;
import br.edu.ifpb.es.daw.model.Avaliacao;
import br.edu.ifpb.es.daw.model.Cliente;
import br.edu.ifpb.es.daw.model.Produto;
import br.edu.ifpb.es.daw.rest.dto.request.AvaliacaoRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.AvaliacaoResponseDTO;
import br.edu.ifpb.es.daw.service.AvaliacaoService;
import br.edu.ifpb.es.daw.service.ClienteService;
import br.edu.ifpb.es.daw.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/avaliacoes")
public class AvaliacaoRestController implements AvaliacaoRestControllerApi {

    @Autowired private AvaliacaoMapper mapper;
    @Autowired private AvaliacaoService service;
    @Autowired private ClienteService clienteService;
    @Autowired private ProdutoService produtoService;

    @Override @GetMapping
    public ResponseEntity<List<AvaliacaoResponseDTO>> listar() {
        return ResponseEntity.ok(service.recuperarTodos().stream().map(mapper::from).toList());
    }

    @Override @PostMapping
    public ResponseEntity<AvaliacaoResponseDTO> adicionar(@RequestBody @Valid AvaliacaoRequestDTO dto) {
        Avaliacao obj = mapper.from(dto);
        Cliente cliente = clienteService.buscarPorId(dto.getIdCliente())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Cliente", dto.getIdCliente()));
        Produto produto = produtoService.buscarPorId(dto.getIdProduto())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Produto", dto.getIdProduto()));
        obj.setCliente(cliente);
        obj.setProduto(produto);
        return new ResponseEntity<>(mapper.from(service.criar(obj)), HttpStatus.CREATED);
    }

    @Override @GetMapping("/{id}")
    public ResponseEntity<AvaliacaoResponseDTO> recuperarPor(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.from(
                service.buscarPorId(id).orElseThrow(() -> new EntidadeNaoEncontradaException("Avaliacao", id))));
    }

    @Override @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        service.remover(service.buscarPorId(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Avaliacao", id)));
        return ResponseEntity.noContent().build();
    }
}