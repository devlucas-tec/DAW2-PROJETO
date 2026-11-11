package br.edu.ifpb.es.daw.rest;

import br.edu.ifpb.es.daw.rest.dto.response.DashboardDTOs.DashboardAdmin;
import br.edu.ifpb.es.daw.rest.dto.response.DashboardDTOs.DashboardVendedor;
import br.edu.ifpb.es.daw.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
@Tag(name = "Dashboards", description = "Métricas dos painéis do vendedor e do administrador")
public class DashboardRestController {

    @Autowired private DashboardService service;

    @GetMapping("/vendedor")
    @PreAuthorize("hasAuthority('VENDEDOR')")
    @Operation(summary = "Métricas do vendedor logado (apenas itens dos produtos dele)")
    public ResponseEntity<DashboardVendedor> vendedor(
            @Parameter(description = "7d, 30d (padrão) ou 12m") @RequestParam(required = false) String periodo) {
        return ResponseEntity.ok(service.vendedor(periodo));
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Métricas gerais da plataforma")
    public ResponseEntity<DashboardAdmin> admin(
            @Parameter(description = "7d, 30d (padrão) ou 12m") @RequestParam(required = false) String periodo) {
        return ResponseEntity.ok(service.admin(periodo));
    }
}
