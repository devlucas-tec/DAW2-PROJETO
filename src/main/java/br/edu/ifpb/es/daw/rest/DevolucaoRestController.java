package br.edu.ifpb.es.daw.rest;

import br.edu.ifpb.es.daw.exception.EntidadeNaoEncontradaException;
import br.edu.ifpb.es.daw.mapper.DevolucaoMapper;
import br.edu.ifpb.es.daw.model.Devolucao;
import br.edu.ifpb.es.daw.model.Pedido;
import br.edu.ifpb.es.daw.rest.dto.request.DevolucaoRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.DevolucaoResponseDTO;
import br.edu.ifpb.es.daw.service.DevolucaoService;
import br.edu.ifpb.es.daw.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/devolucoes")
public class DevolucaoRestController implements DevolucaoRestControllerApi {

    @Autowired private DevolucaoMapper mapper;
    @Autowired private DevolucaoService service;
    @Autowired private PedidoService pedidoService;

    @Override @PostMapping
    public ResponseEntity<DevolucaoResponseDTO> adicionar(@RequestBody @Valid DevolucaoRequestDTO dto) {
        Devolucao obj = mapper.from(dto);
        Pedido pedido = pedidoService.buscarPorId(dto.getIdPedido())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Pedido", dto.getIdPedido()));
        obj.setPedido(pedido);
        return new ResponseEntity<>(mapper.from(service.criar(obj)), HttpStatus.CREATED);
    }

    @Override @GetMapping("/{id}")
    public ResponseEntity<DevolucaoResponseDTO> recuperarPor(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.from(validarExiste(id)));
    }

    @Override @PatchMapping("/{id}/aprovar")
    public ResponseEntity<DevolucaoResponseDTO> aprovar(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.from(service.aprovar(validarExiste(id))));
    }

    @Override @PatchMapping("/{id}/rejeitar")
    public ResponseEntity<DevolucaoResponseDTO> rejeitar(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.from(service.rejeitar(validarExiste(id))));
    }

    private Devolucao validarExiste(Long id) {
        return service.buscarPorId(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Devolucao", id));
    }
}