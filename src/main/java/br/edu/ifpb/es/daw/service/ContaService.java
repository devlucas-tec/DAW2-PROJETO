package br.edu.ifpb.es.daw.service;

import br.edu.ifpb.es.daw.config.StartupListener;
import br.edu.ifpb.es.daw.exception.EntidadeNaoEncontradaException;
import br.edu.ifpb.es.daw.exception.EstadoInvalidoException;
import br.edu.ifpb.es.daw.model.Cliente;
import br.edu.ifpb.es.daw.model.Produto;
import br.edu.ifpb.es.daw.model.Usuario;
import br.edu.ifpb.es.daw.model.Vendedor;
import br.edu.ifpb.es.daw.model.enums.Role;
import br.edu.ifpb.es.daw.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Exclusão de contas (pelo próprio usuário ou pelo ADMIN).
 *
 * <p>Pedidos antigos precisam continuar mostrando quem comprou e o que foi vendido. Por isso:
 * <ul>
 *   <li>conta sem nenhum histórico de compra/venda é apagada de vez, junto com tudo o que pertence a ela
 *       (produtos do vendedor; carrinho, endereços e favoritos do cliente);</li>
 *   <li>conta com histórico é anonimizada: nome, e-mail e documentos são trocados, o login fica bloqueado
 *       e os produtos do vendedor saem da vitrine, dos carrinhos e dos favoritos. Produtos nunca vendidos são apagados.</li>
 * </ul>
 */
@Service
public class ContaService {

    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private ClienteRepository clienteRepository;
    @Autowired private VendedorRepository vendedorRepository;
    @Autowired private ProdutoRepository produtoRepository;
    @Autowired private ItemPedidoRepository itemPedidoRepository;
    @Autowired private ItemCarrinhoRepository itemCarrinhoRepository;
    @Autowired private CarrinhoRepository carrinhoRepository;
    @Autowired private EnderecoRepository enderecoRepository;
    @Autowired private FavoritoRepository favoritoRepository;
    @Autowired private PedidoRepository pedidoRepository;
    @Autowired private NotificacaoService notificacaoService;
    @Autowired private UsuarioAutenticadoService usuarioAutenticado;
    @Autowired private PasswordEncoder passwordEncoder;

    /** O usuário logado exclui a própria conta, confirmando a senha. */
    @Transactional
    public void excluirPropriaConta(String senha) {
        Usuario usuario = usuarioAutenticado.getUsuario();
        if (senha == null || !passwordEncoder.matches(senha, usuario.getSenha())) {
            throw new IllegalArgumentException("A senha está incorreta.");
        }
        excluir(usuario);
    }

    /** Exclusão feita pelo ADMIN (DELETE /clientes/{id} e /vendedores/{id}). */
    @Transactional
    public void excluirPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .filter(u -> !u.isExcluido())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Usuario", id));
        excluir(usuario);
    }

    private void excluir(Usuario usuario) {
        switch (usuario.getRole()) {
            case ADMIN -> excluirAdmin(usuario);
            case VENDEDOR -> excluirVendedor(vendedorRepository.findById(usuario.getId()).orElseThrow());
            case CLIENTE -> excluirCliente(clienteRepository.findById(usuario.getId()).orElseThrow());
        }
    }

    private void excluirAdmin(Usuario admin) {
        if (StartupListener.ADMIN_EMAIL.equalsIgnoreCase(admin.getEmail())) {
            throw new EstadoInvalidoException("O admin padrão não pode ser excluído.");
        }
        if (admin.isAtivo() && usuarioRepository.countByRoleAndAtivoTrueAndExcluidoFalse(Role.ADMIN) <= 1) {
            throw new EstadoInvalidoException("Não é possível excluir o último administrador ativo.");
        }
        notificacaoService.removerDoUsuario(admin.getId());
        usuarioRepository.delete(admin);
        usuarioRepository.flush();
    }

    private void excluirVendedor(Vendedor vendedor) {
        if (pedidoRepository.vendedorTemPedidosEmAndamento(vendedor.getId())) {
            throw new EstadoInvalidoException("Há pedidos em andamento com produtos desta loja. "
                    + "Entregue ou cancele esses pedidos antes de excluir a conta.");
        }

        List<Produto> produtos = produtoRepository.findByVendedorId(vendedor.getId());
        List<Long> idsProdutos = produtos.stream().map(Produto::getId).toList();
        if (!idsProdutos.isEmpty()) {
            itemCarrinhoRepository.removerProdutosDosCarrinhos(idsProdutos);
            favoritoRepository.removerDosProdutos(idsProdutos);
        }
        // Produtos nunca vendidos vão junto com a loja; os vendidos ficam (pausados) para o histórico dos pedidos.
        List<Produto> naoVendidos = produtos.stream()
                .filter(p -> !itemPedidoRepository.existsByProdutoId(p.getId()))
                .toList();
        produtoRepository.deleteAll(naoVendidos);
        produtoRepository.pausarTodosDoVendedor(vendedor.getId());
        notificacaoService.removerDoUsuario(vendedor.getId());

        if (naoVendidos.size() == produtos.size() && !itemPedidoRepository.existsByProdutoVendedorId(vendedor.getId())) {
            vendedorRepository.delete(vendedor);
            vendedorRepository.flush();
            return;
        }
        anonimizar(vendedor);
        vendedor.setRazaoSocial("Loja encerrada");
        vendedor.setCnpjCpf(("EXC" + vendedor.getId()).substring(0, Math.min(18, ("EXC" + vendedor.getId()).length())));
        vendedor.setDescricao(null);
        vendedor.setBannerUrl(null);
        vendedor.setLojaAberta(false);
        vendedorRepository.save(vendedor);
    }

    private void excluirCliente(Cliente cliente) {
        favoritoRepository.removerDoCliente(cliente.getId());
        carrinhoRepository.findByClienteId(cliente.getId()).ifPresent(carrinho -> {
            cliente.setCarrinho(null);
            carrinhoRepository.delete(carrinho);
        });
        enderecoRepository.removerDoCliente(cliente.getId());
        notificacaoService.removerDoUsuario(cliente.getId());

        if (!pedidoRepository.existsByClienteId(cliente.getId())) {
            clienteRepository.delete(cliente);
            clienteRepository.flush();
            return;
        }
        anonimizar(cliente);
        cliente.setTelefone(null);
        clienteRepository.save(cliente);
    }

    /** Troca os dados pessoais e bloqueia o login. O e-mail aleatório libera o original para um novo cadastro. */
    private void anonimizar(Usuario usuario) {
        usuario.setNome("Usuário removido");
        usuario.setEmail("excluido-" + usuario.getId() + "-" + UUID.randomUUID().toString().substring(0, 8)
                + "@removido.invalid");
        usuario.setSenha(passwordEncoder.encode(UUID.randomUUID().toString()));
        usuario.setAtivo(false);
        usuario.setExcluido(true);
        usuario.setDataExclusao(LocalDateTime.now());
    }
}
