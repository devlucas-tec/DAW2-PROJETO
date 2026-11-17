package br.edu.ifpb.es.daw.rest;

import br.edu.ifpb.es.daw.rest.dto.response.PodeAvaliarDTO;
import br.edu.ifpb.es.daw.rest.dto.response.AvaliacaoResumoDTO;
import io.swagger.v3.oas.annotations.Operation;
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

    /** Com idProduto é público (liberado no SecurityConfiguration); sem idProduto, só ADMIN. */
    @Override @GetMapping
    public ResponseEntity<Page<AvaliacaoResponseDTO>> listar(
            @RequestParam(required = false) Long idProduto,
            @RequestParam(defaultValue = "0") int page) {
        Page<AvaliacaoResponseDTO> pagina = idProduto != null
                ? service.recuperarPorProduto(idProduto, page).map(mapper::from)
                : service.recuperarTodos(page).map(mapper::from);
        return ResponseEntity.ok(pagina);
    }

    @GetMapping("/resumo")
    @Operation(summary = "Resumo das avaliações de um produto (público): média, total e quantidade por nota (histograma)")
    public ResponseEntity<AvaliacaoResumoDTO> resumo(@RequestParam Long idProduto) {
        return ResponseEntity.ok(service.resumo(idProduto));
    }

    @GetMapping("/pode-avaliar")
    @PreAuthorize("hasAuthority('CLIENTE')")
    @Operation(summary = "Se o cliente logado pode avaliar o produto (recebeu e ainda não avaliou)")
    public ResponseEntity<PodeAvaliarDTO> podeAvaliar(@RequestParam Long idProduto) {
        return ResponseEntity.ok(service.podeAvaliar(idProduto));
    }

    @GetMapping("/recebidas")
    @PreAuthorize("hasAuthority('VENDEDOR')")
    @Operation(summary = "Avaliações dos produtos do vendedor logado, mais recentes primeiro")
    public ResponseEntity<Page<AvaliacaoResponseDTO>> recebidas(@RequestParam(defaultValue = "0") int page) {
        return ResponseEntity.ok(service.recuperarRecebidas(page).map(mapper::from));
    }

    @Override @GetMapping("/minhas")
    @PreAuthorize("hasAuthority('CLIENTE')")
    public ResponseEntity<Page<AvaliacaoResponseDTO>> minhas(@RequestParam(defaultValue = "0") int page) {
        return ResponseEntity.ok(service.recuperarMinhas(page).map(mapper::from));
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
