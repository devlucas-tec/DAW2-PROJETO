package br.edu.ifpb.es.daw.rest;

import br.edu.ifpb.es.daw.exception.EntidadeNaoEncontradaException;
import br.edu.ifpb.es.daw.mapper.PedidoMapper;
import br.edu.ifpb.es.daw.model.Cliente;
import br.edu.ifpb.es.daw.model.Cupom;
import br.edu.ifpb.es.daw.model.Pedido;
import br.edu.ifpb.es.daw.rest.dto.request.PedidoRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.PedidoResponseDTO;
import br.edu.ifpb.es.daw.service.ClienteService;
import br.edu.ifpb.es.daw.service.CupomService;
import br.edu.ifpb.es.daw.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoRestController implements PedidoRestControllerApi {

    @Autowired private PedidoMapper mapper;
    @Autowired private PedidoService service;
    @Autowired private ClienteService clienteService;
    @Autowired private CupomService cupomService;

    @Override @GetMapping
    public ResponseEntity<List<PedidoResponseDTO>> listar() {
        return ResponseEntity.ok(service.recuperarTodos().stream().map(mapper::from).toList());
    }

    @Override @PostMapping
    public ResponseEntity<PedidoResponseDTO> adicionar(@RequestBody @Valid PedidoRequestDTO dto) {
        Pedido obj = mapper.from(dto);
        Cliente cliente = clienteService.buscarPorId(dto.getIdCliente())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Cliente", dto.getIdCliente()));
        obj.setCliente(cliente);
        if (dto.getIdCupom() != null) {
            Cupom cupom = cupomService.buscarPorId(dto.getIdCupom())
                    .orElseThrow(() -> new EntidadeNaoEncontradaException("Cupom", dto.getIdCupom()));
            obj.setCupom(cupom);
        }
        return new ResponseEntity<>(mapper.from(service.criar(obj)), HttpStatus.CREATED);
    }

    @Override @GetMapping("/{id}")
    public ResponseEntity<PedidoResponseDTO> recuperarPor(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.from(validarExiste(id)));
    }

    @Override @PatchMapping("/{id}/fechar")
    public ResponseEntity<PedidoResponseDTO> fechar(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.from(service.fechar(validarExiste(id))));
    }

    @Override @PatchMapping("/{id}/cancelar")
    public ResponseEntity<PedidoResponseDTO> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.from(service.cancelar(validarExiste(id))));
    }

    @Override @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        service.remover(validarExiste(id));
        return ResponseEntity.noContent().build();
    }

    private Pedido validarExiste(Long id) {
        return service.buscarPorId(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Pedido", id));
    }
}