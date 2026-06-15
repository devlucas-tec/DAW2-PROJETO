package br.edu.ifpb.es.daw.rest;

import br.edu.ifpb.es.daw.exception.EntidadeNaoEncontradaException;
import br.edu.ifpb.es.daw.mapper.CategoriaMapper;
import br.edu.ifpb.es.daw.model.Categoria;
import br.edu.ifpb.es.daw.rest.dto.request.CategoriaRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.CategoriaResponseDTO;
import br.edu.ifpb.es.daw.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categorias")
public class CategoriaRestController implements CategoriaRestControllerApi {

    @Autowired private CategoriaMapper mapper;
    @Autowired private CategoriaService service;

    @Override @GetMapping
    public ResponseEntity<Page<CategoriaResponseDTO>> listar(
            @RequestParam(defaultValue = "0") int page) {
        return ResponseEntity.ok(service.recuperarTodos(page).map(mapper::from));
    }

    @Override @PostMapping
    public ResponseEntity<CategoriaResponseDTO> adicionar(@RequestBody @Valid CategoriaRequestDTO dto) {
        return new ResponseEntity<>(mapper.from(service.criar(mapper.from(dto))), HttpStatus.CREATED);
    }

    @Override @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> recuperarPor(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.from(validarExiste(id)));
    }

    @Override @PatchMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> atualizar(@PathVariable Long id, @RequestBody @Valid CategoriaRequestDTO dto) {
        Categoria obj = validarExiste(id);
        Categoria atualizado = mapper.from(dto);
        atualizado.setId(obj.getId());
        return ResponseEntity.ok(mapper.from(service.atualizar(atualizado)));
    }

    @Override @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        service.remover(validarExiste(id));
        return ResponseEntity.noContent().build();
    }

    private Categoria validarExiste(Long id) {
        return service.buscarPorId(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Categoria", id));
    }
}
