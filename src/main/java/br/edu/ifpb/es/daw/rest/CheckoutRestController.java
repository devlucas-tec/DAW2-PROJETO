package br.edu.ifpb.es.daw.rest;

import br.edu.ifpb.es.daw.mapper.CompraMapper;
import br.edu.ifpb.es.daw.rest.dto.request.CheckoutRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.CompraResponseDTO;
import br.edu.ifpb.es.daw.service.CheckoutService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Checkout", description = "Finalização da compra e consulta das compras")
public class CheckoutRestController {

    @Autowired private CheckoutService service;
    @Autowired private CompraMapper mapper;

    @PostMapping("/checkout")
    @PreAuthorize("hasAuthority('CLIENTE')")
    @Operation(summary = "Finalizar a compra: transforma o carrinho em um pedido por vendedor")
    public ResponseEntity<CompraResponseDTO> finalizar(@RequestBody @Valid CheckoutRequestDTO dto) {
        return new ResponseEntity<>(mapper.from(service.finalizar(dto)), HttpStatus.CREATED);
    }

    @GetMapping("/compras")
    @PreAuthorize("hasAuthority('CLIENTE')")
    @Operation(summary = "Listar as compras do cliente logado")
    public ResponseEntity<Page<CompraResponseDTO>> minhas(@RequestParam(defaultValue = "0") int page) {
        return ResponseEntity.ok(service.minhas(page).map(mapper::from));
    }

    @GetMapping("/compras/{codigo}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CLIENTE')")
    @Operation(summary = "Buscar compra pelo código")
    public ResponseEntity<CompraResponseDTO> recuperarPor(@PathVariable String codigo) {
        return ResponseEntity.ok(mapper.from(service.buscarPorCodigo(codigo)));
    }
}
