package br.edu.ifpb.es.daw.service;

import br.edu.ifpb.es.daw.model.ItemPedido;
import br.edu.ifpb.es.daw.model.Pedido;
import br.edu.ifpb.es.daw.model.Usuario;
import br.edu.ifpb.es.daw.model.enums.Role;
import br.edu.ifpb.es.daw.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class UsuarioAutenticadoService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario getUsuario() {
        return getUsuarioOpcional()
                .orElseThrow(() -> new AccessDeniedException("Usuário não autenticado."));
    }

    /** Usuário do token, se houver. Usado nos endpoints públicos, que também aceitam token. */
    public Optional<Usuario> getUsuarioOpcional() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }
        return usuarioRepository.findByEmail(authentication.getName());
    }

    public boolean isAdmin() {
        return getUsuarioOpcional().map(usuario -> usuario.getRole() == Role.ADMIN).orElse(false);
    }

    public Long resolverIdUsuario(Long idInformado, String campo) {
        Usuario usuario = getUsuario();
        if (usuario.getRole() == Role.ADMIN) {
            if (idInformado == null) {
                throw new IllegalArgumentException("O campo '" + campo + "' é obrigatório para ADMIN.");
            }
            return idInformado;
        }
        if (idInformado != null && !Objects.equals(idInformado, usuario.getId())) {
            throw new AccessDeniedException("Você só pode realizar esta operação em seu próprio nome.");
        }
        return usuario.getId();
    }

    public void validarProprioUsuario(Long idUsuario) {
        Usuario usuario = getUsuario();
        if (usuario.getRole() != Role.ADMIN && !Objects.equals(usuario.getId(), idUsuario)) {
            throw new AccessDeniedException("Você só pode acessar ou alterar os seus próprios dados.");
        }
    }

    public void validarAcessoPedido(Pedido pedido) {
        if (!podeAcessarPedido(pedido)) {
            throw new AccessDeniedException("Você não tem acesso a este pedido.");
        }
    }

    public void validarDonoPedido(Pedido pedido) {
        Usuario usuario = getUsuario();
        if (usuario.getRole() != Role.ADMIN && !Objects.equals(usuario.getId(), pedido.getCliente().getId())) {
            throw new AccessDeniedException("Apenas o cliente dono do pedido pode realizar esta operação.");
        }
    }

    /** Só o ADMIN ou o vendedor dono de todos os itens do pedido podem avançar o status. */
    public void validarVendedorDoPedido(Pedido pedido) {
        Usuario usuario = getUsuario();
        if (usuario.getRole() == Role.ADMIN) {
            return;
        }
        boolean dono = usuario.getRole() == Role.VENDEDOR
                && !pedido.getItens().isEmpty()
                && pedido.getItens().stream()
                .map(ItemPedido::getProduto)
                .allMatch(produto -> Objects.equals(produto.getVendedor().getId(), usuario.getId()));
        if (!dono) {
            throw new AccessDeniedException("Apenas o vendedor dos itens deste pedido pode atualizar o seu status.");
        }
    }

    public boolean podeAcessarPedido(Pedido pedido) {
        Usuario usuario = getUsuario();
        return switch (usuario.getRole()) {
            case ADMIN -> true;
            case CLIENTE -> Objects.equals(usuario.getId(), pedido.getCliente().getId());
            case VENDEDOR -> pedido.getItens().stream()
                    .map(ItemPedido::getProduto)
                    .anyMatch(produto -> Objects.equals(produto.getVendedor().getId(), usuario.getId()));
        };
    }
}
