package br.edu.ifpb.es.daw.mapper;

import br.edu.ifpb.es.daw.model.Categoria;
import br.edu.ifpb.es.daw.rest.dto.request.CategoriaRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.CategoriaResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class CategoriaMapper {
    public CategoriaResponseDTO from(Categoria obj) {
        CategoriaResponseDTO dto = new CategoriaResponseDTO();
        dto.setId(obj.getId());
        dto.setNome(obj.getNome());
        dto.setDescricao(obj.getDescricao());
        return dto;
    }
    public Categoria from(CategoriaRequestDTO dto) {
        Categoria obj = new Categoria();
        obj.setNome(dto.getNome());
        obj.setDescricao(dto.getDescricao());
        return obj;
    }
}