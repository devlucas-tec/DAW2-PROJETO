package br.edu.ifpb.es.daw.rest;

import br.edu.ifpb.es.daw.exception.EntidadeNaoEncontradaException;
import br.edu.ifpb.es.daw.mapper.CupomMapper;
import br.edu.ifpb.es.daw.model.Cupom;
import br.edu.ifpb.es.daw.rest.dto.request.CupomRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.CupomResponseDTO;
import br.edu.ifpb.es.daw.service.CupomService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/cupons")
public class CupomRestController implements CupomRestControllerApi {

    @Autowired private CupomMapper mapper;
    @Autowired private CupomService service;

    @Override @GetMapping
    public ResponseEntity<List<CupomResponseDTO>> listar() {
        return ResponseEntity.ok(service.recuperarTodos().stream().map(mapper::from).toList());
    }

    @Override @PostMapping
    public ResponseEntity<CupomResponseDTO> adicionar(@RequestBody @Valid CupomRequestDTO dto) {
        return new ResponseEntity<>(mapper.from(service.criar(mapper.from(dto))), HttpStatus.CREATED);
    }

    @Override @GetMapping("/{id}")
    public ResponseEntity<CupomResponseDTO> recuperarPor(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.from(validarExiste(id)));
    }

    @Override @PatchMapping("/{id}")
    public ResponseEntity<CupomResponseDTO> atualizar(@PathVariable Long id, @RequestBody @Valid CupomRequestDTO dto) {
        Cupom obj = validarExiste(id);
        Cupom atualizado = mapper.from(dto);
        atualizado.setId(obj.getId());
        return ResponseEntity.ok(mapper.from(service.atualizar(atualizado)));
    }

    @Override @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        service.remover(validarExiste(id));
        return ResponseEntity.noContent().build();
    }

    private Cupom validarExiste(Long id) {
        return service.buscarPorId(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Cupom", id));
    }
}