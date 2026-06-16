package br.edu.ifpb.es.daw.rest;

import br.edu.ifpb.es.daw.exception.EntidadeNaoEncontradaException;
import br.edu.ifpb.es.daw.mapper.PagamentoMapper;
import br.edu.ifpb.es.daw.model.Pagamento;
import br.edu.ifpb.es.daw.model.Pedido;
import br.edu.ifpb.es.daw.rest.dto.request.PagamentoRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.PagamentoResponseDTO;
import br.edu.ifpb.es.daw.service.PagamentoService;
import br.edu.ifpb.es.daw.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pagamentos")
public class PagamentoRestController implements PagamentoRestControllerApi {

    @Autowired private PagamentoMapper mapper;
    @Autowired private PagamentoService service;
    @Autowired private PedidoService pedidoService;

    @Override @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CLIENTE')")
    public ResponseEntity<PagamentoResponseDTO> adicionar(@RequestBody @Valid PagamentoRequestDTO dto) {
        Pagamento obj = mapper.from(dto);
        Pedido pedido = pedidoService.buscarPorId(dto.getIdPedido())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Pedido", dto.getIdPedido()));
        obj.setPedido(pedido);
        return new ResponseEntity<>(mapper.from(service.criar(obj)), HttpStatus.CREATED);
    }

    @Override @GetMapping("/{id}")
    public ResponseEntity<PagamentoResponseDTO> recuperarPor(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.from(validarExiste(id)));
    }

    @Override @PatchMapping("/{id}/confirmar")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'VENDEDOR')")
    public ResponseEntity<PagamentoResponseDTO> confirmar(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.from(service.confirmar(validarExiste(id))));
    }

    @Override @PatchMapping("/{id}/cancelar")
    public ResponseEntity<PagamentoResponseDTO> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.from(service.cancelar(validarExiste(id))));
    }

    private Pagamento validarExiste(Long id) {
        return service.buscarPorId(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Pagamento", id));
    }
}