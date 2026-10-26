package br.edu.ifpb.es.daw.service;

import br.edu.ifpb.es.daw.exception.ConflitoException;
import br.edu.ifpb.es.daw.exception.EntidadeNaoEncontradaException;
import br.edu.ifpb.es.daw.model.Cupom;
import br.edu.ifpb.es.daw.model.enums.StatusCupom;
import br.edu.ifpb.es.daw.repository.CupomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class CupomService {

    private static final int PAGE_SIZE = 10;

    @Autowired
    private CupomRepository repository;

    @Transactional
    public Cupom criar(Cupom obj, StatusCupom status) {
        validarCodigoDisponivel(obj.getCodigo(), null);
        obj.setStatus(status != null ? status : StatusCupom.ATIVO);
        return repository.save(obj);
    }

    public Page<Cupom> recuperarTodos(int page) {
        return repository.findAll(PageRequest.of(page, PAGE_SIZE));
    }

    public Page<Cupom> filtrar(String codigo, StatusCupom status, int page) {
        return repository.filtrar(
                codigo != null && codigo.isBlank() ? null : codigo,
                status,
                PageRequest.of(page, PAGE_SIZE)
        );
    }

    public Cupom buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Cupom", id));
    }

    @Transactional
    public Cupom atualizar(Long id, Cupom dados, StatusCupom status) {
        Cupom cupom = buscarPorId(id);
        validarCodigoDisponivel(dados.getCodigo(), id);
        cupom.setCodigo(dados.getCodigo());
        cupom.setValorDesconto(dados.getValorDesconto());
        cupom.setDataExpiracao(dados.getDataExpiracao());
        if (status != null) {
            cupom.setStatus(status);
        }
        return repository.save(cupom);
    }

    @Transactional
    public void remover(Long id) {
        repository.delete(buscarPorId(id));
        repository.flush();
    }

    private void validarCodigoDisponivel(String codigo, Long idAtual) {
        repository.findByCodigoIgnoreCase(codigo)
                .filter(existente -> !Objects.equals(existente.getId(), idAtual))
                .ifPresent(existente -> {
                    throw new ConflitoException("Já existe um cupom com o código '" + codigo + "'.");
                });
    }
}
