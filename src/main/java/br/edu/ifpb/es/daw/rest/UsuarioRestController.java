package br.edu.ifpb.es.daw.rest;

import org.springframework.web.bind.annotation.DeleteMapping;
import br.edu.ifpb.es.daw.service.ContaService;
import br.edu.ifpb.es.daw.rest.dto.request.ExcluirContaDTO;
import br.edu.ifpb.es.daw.model.Usuario;
import br.edu.ifpb.es.daw.model.enums.Role;
import br.edu.ifpb.es.daw.rest.dto.request.AlterarAtivoDTO;
import br.edu.ifpb.es.daw.rest.dto.response.UsuarioAdminResponseDTO;
import br.edu.ifpb.es.daw.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
@PreAuthorize("hasAuthority('ADMIN')")
@Tag(name = "Usuários", description = "Gestão de usuários pelo administrador")
public class UsuarioRestController {

    @Autowired private UsuarioService service;
    @Autowired private ContaService contaService;

    @GetMapping
    @Operation(summary = "Listar usuários, com filtro por nome/e-mail, papel e situação")
    public ResponseEntity<Page<UsuarioAdminResponseDTO>> listar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Role role,
            @RequestParam(required = false) Boolean ativo,
            @RequestParam(defaultValue = "0") int page) {
        return ResponseEntity.ok(service.filtrar(nome, role, ativo, page).map(UsuarioRestController::from));
    }

    @PatchMapping("/{id}/ativo")
    @Operation(summary = "Bloquear (ativo=false) ou desbloquear um usuário. "
            + "Usuário bloqueado não loga e os produtos de um vendedor bloqueado somem da vitrine")
    public ResponseEntity<UsuarioAdminResponseDTO> alterarAtivo(@PathVariable Long id,
                                                                @RequestBody @Valid AlterarAtivoDTO dto) {
        return ResponseEntity.ok(from(service.alterarAtivo(id, dto.getAtivo())));
    }

    @DeleteMapping("/me")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Excluir a própria conta (qualquer perfil), confirmando a senha. "
            + "Contas sem histórico são apagadas com tudo o que pertence a elas (o vendedor leva os produtos junto); "
            + "contas com pedidos são anonimizadas e os produtos vendidos saem da vitrine")
    public ResponseEntity<Void> excluirMinhaConta(@RequestBody @Valid ExcluirContaDTO dto) {
        contaService.excluirPropriaConta(dto.getSenha());
        return ResponseEntity.noContent().build();
    }

    private static UsuarioAdminResponseDTO from(Usuario usuario) {
        return new UsuarioAdminResponseDTO(usuario.getId(), usuario.getNome(), usuario.getEmail(),
                usuario.getRole().name(), usuario.isAtivo(), usuario.getDataCadastro());
    }
}
