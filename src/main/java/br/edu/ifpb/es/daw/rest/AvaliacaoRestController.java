package br.edu.ifpb.es.daw.rest;

import br.edu.ifpb.es.daw.mapper.AvaliacaoMapper;
import br.edu.ifpb.es.daw.model.Cliente;
import br.edu.ifpb.es.daw.model.Produto;
import br.edu.ifpb.es.daw.rest.dto.request.AvaliacaoRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.AvaliacaoResponseDTO;
import br.edu.ifpb.es.daw.service.AvaliacaoService;
import br.edu.ifpb.es.daw.service.ClienteService;
import br.edu.ifpb.es.daw.service.ProdutoService;
import br.edu.ifpb.es.daw.service.UsuarioAutenticadoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/avaliacoes")
public class AvaliacaoRestController implements AvaliacaoRestControllerApi {

    @Autowired private AvaliacaoMapper mapper;
    @Autowired private AvaliacaoService service;
    @Autowired private ClienteService clienteService;
    @Autowired private ProdutoService produtoService;
    @Autowired private UsuarioAutenticadoService usuarioAutenticado;

    @Override @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'VENDEDOR', 'CLIENTE')")
    public ResponseEntity<Page<AvaliacaoResponseDTO>> listar(@RequestParam(defaultValue = "0") int page) {
        return ResponseEntity.ok(service.recuperarTodos(page).map(mapper::from));
    }

    @Override @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CLIENTE')")
    public ResponseEntity<AvaliacaoResponseDTO> adicionar(@RequestBody @Valid AvaliacaoRequestDTO dto) {
        Long idCliente = usuarioAutenticado.resolverIdUsuario(dto.getIdCliente(), "idCliente");
        Cliente cliente = clienteService.buscarPorId(idCliente);
        Produto produto = produtoService.buscarPorId(dto.getIdProduto());
        return new ResponseEntity<>(mapper.from(service.criar(mapper.from(dto), cliente, produto)), HttpStatus.CREATED);
    }

    @Override @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'VENDEDOR', 'CLIENTE')")
    public ResponseEntity<AvaliacaoResponseDTO> recuperarPor(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.from(service.buscarPorId(id)));
    }

    @Override @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CLIENTE')")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        service.remover(id);
        return ResponseEntity.noContent().build();
    }
}
