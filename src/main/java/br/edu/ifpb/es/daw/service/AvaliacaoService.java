package br.edu.ifpb.es.daw.service;

import br.edu.ifpb.es.daw.exception.ConflitoException;
import br.edu.ifpb.es.daw.exception.EntidadeNaoEncontradaException;
import br.edu.ifpb.es.daw.exception.EstadoInvalidoException;
import br.edu.ifpb.es.daw.model.Avaliacao;
import br.edu.ifpb.es.daw.model.Cliente;
import br.edu.ifpb.es.daw.model.Produto;
import br.edu.ifpb.es.daw.repository.AvaliacaoRepository;
import br.edu.ifpb.es.daw.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AvaliacaoService {

    private static final int PAGE_SIZE = 10;

    @Autowired
    private AvaliacaoRepository repository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private UsuarioAutenticadoService usuarioAutenticado;

    @Transactional
    public Avaliacao criar(Avaliacao obj, Cliente cliente, Produto produto) {
        if (!pedidoRepository.clienteComprouProduto(cliente.getId(), produto.getId())) {
            throw new EstadoInvalidoException("O cliente só pode avaliar produtos de pedidos CONFIRMADOS.");
        }
        if (repository.existsByClienteAndProduto(cliente, produto)) {
            throw new ConflitoException("O cliente já avaliou este produto.");
        }
        obj.setCliente(cliente);
        obj.setProduto(produto);
        return repository.save(obj);
    }

    public Page<Avaliacao> recuperarTodos(int page) {
        return repository.findAll(PageRequest.of(page, PAGE_SIZE));
    }

    public Avaliacao buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Avaliacao", id));
    }

    @Transactional
    public void remover(Long id) {
        Avaliacao avaliacao = buscarPorId(id);
        usuarioAutenticado.validarProprioUsuario(avaliacao.getCliente().getId());
        repository.delete(avaliacao);
    }
}
