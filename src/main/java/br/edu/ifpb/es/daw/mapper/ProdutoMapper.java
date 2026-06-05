package br.edu.ifpb.es.daw.mapper;

import br.edu.ifpb.es.daw.model.Produto;
import br.edu.ifpb.es.daw.rest.dto.request.ProdutoRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.ProdutoResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class ProdutoMapper {

    public ProdutoResponseDTO from(Produto obj) {
        ProdutoResponseDTO dto = new ProdutoResponseDTO();
        dto.setId(obj.getId());
        dto.setNome(obj.getNome());
        dto.setDescricao(obj.getDescricao() != null ? obj.getDescricao() : "");
        dto.setPreco(obj.getPreco());
        dto.setEstoque(obj.getEstoque());
        if (obj.getCategoria() != null) dto.setNomeCategoria(obj.getCategoria().getNome());
        if (obj.getVendedor() != null) dto.setNomeVendedor(obj.getVendedor().getRazaoSocial());
        return dto;
    }

    public Produto from(ProdutoRequestDTO dto) {
        Produto obj = new Produto();
        obj.setNome(dto.getNome());
        obj.setDescricao(dto.getDescricao());
        obj.setPreco(dto.getPreco());
        obj.setEstoque(dto.getEstoque());
        return obj;
    }
}