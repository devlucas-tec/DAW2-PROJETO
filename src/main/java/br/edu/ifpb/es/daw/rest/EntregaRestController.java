package br.edu.ifpb.es.daw.rest;

import br.edu.ifpb.es.daw.exception.EntidadeNaoEncontradaException;
import br.edu.ifpb.es.daw.mapper.EntregaMapper;
import br.edu.ifpb.es.daw.model.Entrega;
import br.edu.ifpb.es.daw.model.Pedido;
import br.edu.ifpb.es.daw.model.enums.StatusEntrega;
import br.edu.ifpb.es.daw.rest.dto.request.EntregaRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.EntregaResponseDTO;
import br.edu.ifpb.es.daw.service.EntregaService;
import br.edu.ifpb.es.daw.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/entregas")
public class EntregaRestController implements EntregaRestControllerApi {

    @Autowired private EntregaMapper mapper;
    @Autowired private EntregaService service;
    @Autowired private PedidoService pedidoService;

    @Override @PostMapping
    public ResponseEntity<EntregaResponseDTO> adicionar(@RequestBody @Valid EntregaRequestDTO dto) {
        Entrega obj = mapper.from(dto);
        Pedido pedido = pedidoService.buscarPorId(dto.getIdPedido())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Pedido", dto.getIdPedido()));
        obj.setPedido(pedido);
        return new ResponseEntity<>(mapper.from(service.criar(obj)), HttpStatus.CREATED);
    }

    @Override @GetMapping("/{id}")
    public ResponseEntity<EntregaResponseDTO> recuperarPor(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.from(validarExiste(id)));
    }

    @Override @PatchMapping("/{id}/status")
    public ResponseEntity<EntregaResponseDTO> atualizarStatus(@PathVariable Long id, @RequestParam StatusEntrega status) {
        return ResponseEntity.ok(mapper.from(service.atualizarStatus(validarExiste(id), status)));
    }

    private Entrega validarExiste(Long id) {
        return service.buscarPorId(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Entrega", id));
    }
}