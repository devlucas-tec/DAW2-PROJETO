package br.edu.ifpb.es.daw.service;

import br.edu.ifpb.es.daw.model.Cliente;
import br.edu.ifpb.es.daw.model.Favorito;
import br.edu.ifpb.es.daw.model.Produto;
import br.edu.ifpb.es.daw.repository.ClienteRepository;
import br.edu.ifpb.es.daw.repository.FavoritoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/** Lista de desejos do cliente logado. Adicionar e remover são idempotentes. */
@Service
public class FavoritoService {

    private static final int PAGE_SIZE = 12;

    @Autowired private FavoritoRepository repository;
    @Autowired private ClienteRepository clienteRepository;
    @Autowired private ProdutoService produtoService;
    @Autowired private UsuarioAutenticadoService usuarioAutenticado;

    /** Produtos favoritados, mais recentes primeiro (inclui os que saíram da vitrine, para o cliente poder removê-los). */
    public Page<Produto> meus(int page) {
        return repository.findByClienteId(idLogado(), PageRequest.of(Math.max(page, 0), PAGE_SIZE,
                        Sort.by(Sort.Direction.DESC, "dataCriacao").and(Sort.by(Sort.Direction.DESC, "id"))))
                .map(Favorito::getProduto);
    }

    /** Só os ids, para o front pintar o coração nos cards sem buscar a lista inteira. */
    public List<Long> idsDosMeus() {
        return repository.idsDosProdutos(idLogado());
    }

    @Transactional
    public void adicionar(Long idProduto) {
        Cliente cliente = clienteLogado();
        if (repository.findByClienteIdAndProdutoId(cliente.getId(), idProduto).isPresent()) {
            return;
        }
        Favorito favorito = new Favorito();
        favorito.setCliente(cliente);
        favorito.setProduto(produtoService.buscarVisivel(idProduto));
        repository.save(favorito);
    }

    @Transactional
    public void remover(Long idProduto) {
        repository.findByClienteIdAndProdutoId(idLogado(), idProduto).ifPresent(repository::delete);
    }

    private Cliente clienteLogado() {
        return clienteRepository.findById(idLogado())
                .orElseThrow(() -> new AccessDeniedException("Apenas clientes possuem lista de desejos."));
    }

    private Long idLogado() {
        return usuarioAutenticado.getUsuario().getId();
    }
}
