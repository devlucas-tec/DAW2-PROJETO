package br.edu.ifpb.es.daw.rest;

import br.edu.ifpb.es.daw.mapper.PedidoMapper;
import br.edu.ifpb.es.daw.model.Cliente;
import br.edu.ifpb.es.daw.model.Cupom;
import br.edu.ifpb.es.daw.model.enums.StatusPedido;
import br.edu.ifpb.es.daw.rest.dto.request.AtualizarStatusPedidoDTO;
import br.edu.ifpb.es.daw.rest.dto.request.PedidoRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.PedidoHistoricoResponseDTO;
import br.edu.ifpb.es.daw.rest.dto.response.PedidoResponseDTO;
import br.edu.ifpb.es.daw.service.CicloPedidoService;
import br.edu.ifpb.es.daw.service.ClienteService;
import br.edu.ifpb.es.daw.service.CupomService;
import br.edu.ifpb.es.daw.service.PedidoService;
import br.edu.ifpb.es.daw.service.UsuarioAutenticadoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoRestController implements PedidoRestControllerApi {

    @Autowired private PedidoMapper mapper;
    @Autowired private PedidoService service;
    @Autowired private CicloPedidoService cicloPedidoService;
    @Autowired private ClienteService clienteService;
    @Autowired private CupomService cupomService;
    @Autowired private UsuarioAutenticadoService usuarioAutenticado;

    @Override @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'VENDEDOR', 'CLIENTE')")
    public ResponseEntity<Page<PedidoResponseDTO>> listar(
            @RequestParam(required = false) StatusPedido status,
            @RequestParam(defaultValue = "0") int page) {
        return ResponseEntity.ok(service.recuperarTodos(status, page).map(mapper::from));
    }

    @Override @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CLIENTE')")
    public ResponseEntity<PedidoResponseDTO> adicionar(@RequestBody @Valid PedidoRequestDTO dto) {
        Long idCliente = usuarioAutenticado.resolverIdUsuario(dto.getIdCliente(), "idCliente");
        Cliente cliente = clienteService.buscarPorId(idCliente);
        Cupom cupom = dto.getIdCupom() != null ? cupomService.buscarPorId(dto.getIdCupom()) : null;
        return new ResponseEntity<>(mapper.from(service.criar(cliente, cupom)), HttpStatus.CREATED);
    }

    @Override @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'VENDEDOR', 'CLIENTE')")
    public ResponseEntity<PedidoResponseDTO> recuperarPor(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.from(service.buscarComAcesso(id)));
    }

    @Override @GetMapping("/{id}/historico")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'VENDEDOR', 'CLIENTE')")
    public ResponseEntity<List<PedidoHistoricoResponseDTO>> historico(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.historico(service.buscarComAcesso(id).getHistorico()));
    }

    @Override @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'VENDEDOR')")
    public ResponseEntity<PedidoResponseDTO> atualizarStatus(@PathVariable Long id,
                                                             @RequestBody @Valid AtualizarStatusPedidoDTO dto) {
        return ResponseEntity.ok(mapper.from(cicloPedidoService.avancar(
                id, dto.getStatus(), dto.getTransportadora(), dto.getCodigoRastreamento())));
    }

    @Override @PatchMapping("/{id}/fechar")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CLIENTE')")
    public ResponseEntity<PedidoResponseDTO> fechar(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.from(service.fechar(id)));
    }

    @Override @PatchMapping("/{id}/cancelar")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CLIENTE')")
    public ResponseEntity<PedidoResponseDTO> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.from(service.cancelar(id)));
    }

    @Override @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        service.remover(id);
        return ResponseEntity.noContent().build();
    }
}
