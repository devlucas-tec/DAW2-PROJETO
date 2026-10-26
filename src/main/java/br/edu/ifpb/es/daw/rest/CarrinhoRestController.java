package br.edu.ifpb.es.daw.rest;

import br.edu.ifpb.es.daw.mapper.CarrinhoMapper;
import br.edu.ifpb.es.daw.rest.dto.request.CarrinhoRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.CarrinhoResponseDTO;
import br.edu.ifpb.es.daw.service.CarrinhoService;
import br.edu.ifpb.es.daw.service.ClienteService;
import br.edu.ifpb.es.daw.service.UsuarioAutenticadoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carrinhos")
@PreAuthorize("hasAnyAuthority('ADMIN', 'CLIENTE')")
public class CarrinhoRestController implements CarrinhoRestControllerApi {

    @Autowired private CarrinhoMapper mapper;
    @Autowired private CarrinhoService service;
    @Autowired private ClienteService clienteService;
    @Autowired private UsuarioAutenticadoService usuarioAutenticado;

    @Override @PostMapping
    public ResponseEntity<CarrinhoResponseDTO> adicionar(@RequestBody @Valid CarrinhoRequestDTO dto) {
        Long idCliente = usuarioAutenticado.resolverIdUsuario(dto.getIdCliente(), "idCliente");
        return new ResponseEntity<>(mapper.from(service.criar(clienteService.buscarPorId(idCliente))), HttpStatus.CREATED);
    }

    @Override @GetMapping("/{id}")
    public ResponseEntity<CarrinhoResponseDTO> recuperarPor(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.from(service.buscarPorId(id)));
    }

    @Override @DeleteMapping("/{id}/limpar")
    public ResponseEntity<CarrinhoResponseDTO> limpar(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.from(service.limpar(id)));
    }
}
