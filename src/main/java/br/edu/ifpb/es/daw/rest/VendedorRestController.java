package br.edu.ifpb.es.daw.rest;

import br.edu.ifpb.es.daw.exception.EntidadeNaoEncontradaException;
import br.edu.ifpb.es.daw.mapper.VendedorMapper;
import br.edu.ifpb.es.daw.model.Vendedor;
import br.edu.ifpb.es.daw.rest.dto.request.VendedorRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.VendedorResponseDTO;
import br.edu.ifpb.es.daw.service.VendedorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vendedores")
public class VendedorRestController implements VendedorRestControllerApi {

    @Autowired private VendedorMapper mapper;
    @Autowired private VendedorService service;

    @Override @GetMapping
    public ResponseEntity<Page<VendedorResponseDTO>> listar(
            @RequestParam(defaultValue = "0") int page) {
        return ResponseEntity.ok(service.recuperarTodos(page).map(mapper::from));
    }

    @Override @PostMapping
    public ResponseEntity<VendedorResponseDTO> adicionar(@RequestBody @Valid VendedorRequestDTO dto) {
        return new ResponseEntity<>(mapper.from(service.criar(mapper.from(dto))), HttpStatus.CREATED);
    }

    @Override @GetMapping("/{id}")
    public ResponseEntity<VendedorResponseDTO> recuperarPor(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.from(validarExiste(id)));
    }

    @Override @PatchMapping("/{id}")
    public ResponseEntity<VendedorResponseDTO> atualizar(@PathVariable Long id, @RequestBody @Valid VendedorRequestDTO dto) {
        Vendedor obj = validarExiste(id);
        Vendedor atualizado = mapper.from(dto);
        atualizado.setId(obj.getId());
        return ResponseEntity.ok(mapper.from(service.atualizar(atualizado)));
    }

    @Override @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        service.remover(validarExiste(id));
        return ResponseEntity.noContent().build();
    }

    private Vendedor validarExiste(Long id) {
        return service.buscarPorId(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Vendedor", id));
    }
}
