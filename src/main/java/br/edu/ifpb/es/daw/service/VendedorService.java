package br.edu.ifpb.es.daw.service;

import br.edu.ifpb.es.daw.exception.ConflitoException;
import br.edu.ifpb.es.daw.exception.EntidadeNaoEncontradaException;
import br.edu.ifpb.es.daw.model.Vendedor;
import br.edu.ifpb.es.daw.model.enums.Role;
import br.edu.ifpb.es.daw.repository.VendedorRepository;
import br.edu.ifpb.es.daw.rest.dto.request.VendedorUpdateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.edu.ifpb.es.daw.repository.AvaliacaoRepository;
import br.edu.ifpb.es.daw.repository.ProdutoRepository;
import br.edu.ifpb.es.daw.rest.dto.response.VendedorPublicoResponseDTO;

import java.util.Objects;

@Service
public class VendedorService {

    private static final int PAGE_SIZE = 10;

    @Autowired
    private VendedorRepository repository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioAutenticadoService usuarioAutenticado;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    @Transactional
    public Vendedor criar(Vendedor obj) {
        usuarioService.validarEmailDisponivel(obj.getEmail(), null);
        validarCnpjCpfDisponivel(obj.getCnpjCpf(), null);
        obj.setSenha(usuarioService.codificarSenha(obj.getSenha()));
        obj.setRole(Role.VENDEDOR);
        return repository.save(obj);
    }

    public Page<Vendedor> recuperarTodos(int page) {
        return repository.findAll(PageRequest.of(page, PAGE_SIZE));
    }

    public Vendedor buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Vendedor", id));
    }

    /** Perfil público da loja. Um vendedor bloqueado só é visível para o ADMIN. */
    public VendedorPublicoResponseDTO buscarPerfilPublico(Long id) {
        Vendedor vendedor = buscarPorId(id);
        if (!vendedor.isAtivo() && !usuarioAutenticado.isAdmin()) {
            throw new EntidadeNaoEncontradaException("Vendedor", id);
        }
        VendedorPublicoResponseDTO dto = new VendedorPublicoResponseDTO();
        dto.setId(vendedor.getId());
        dto.setNome(vendedor.getNome());
        dto.setRazaoSocial(vendedor.getRazaoSocial());
        dto.setDataCadastro(vendedor.getDataCadastro());
        dto.setTotalProdutos(produtoRepository.countByVendedorIdAndAtivoTrue(id));
        long totalAvaliacoes = avaliacaoRepository.countByProdutoVendedorId(id);
        Double media = avaliacaoRepository.mediaPorVendedor(id);
        dto.setTotalAvaliacoes(totalAvaliacoes);
        dto.setMediaAvaliacoes(totalAvaliacoes == 0 || media == null ? null : AvaliacaoService.arredondar(media));
        return dto;
    }

    @Transactional
    public Vendedor atualizar(Long id, VendedorUpdateDTO dto) {
        usuarioAutenticado.validarProprioUsuario(id);
        Vendedor vendedor = buscarPorId(id);
        usuarioService.atualizarDadosBasicos(vendedor, dto.getNome(), dto.getEmail(), dto.getSenha());
        if (dto.getRazaoSocial() != null) {
            vendedor.setRazaoSocial(dto.getRazaoSocial());
        }
        if (dto.getCnpjCpf() != null) {
            validarCnpjCpfDisponivel(dto.getCnpjCpf(), vendedor.getId());
            vendedor.setCnpjCpf(dto.getCnpjCpf());
        }
        return repository.save(vendedor);
    }

    @Transactional
    public void remover(Long id) {
        repository.delete(buscarPorId(id));
        repository.flush();
    }

    private void validarCnpjCpfDisponivel(String cnpjCpf, Long idAtual) {
        repository.findByCnpjCpf(cnpjCpf)
                .filter(existente -> !Objects.equals(existente.getId(), idAtual))
                .ifPresent(existente -> {
                    throw new ConflitoException("Já existe um vendedor cadastrado com o CNPJ/CPF '" + cnpjCpf + "'.");
                });
    }
}
