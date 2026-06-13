package br.edu.ifpb.es.daw.mapper;

import br.edu.ifpb.es.daw.model.Categoria;
import br.edu.ifpb.es.daw.rest.dto.request.CategoriaRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.CategoriaResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoriaMapper {

    CategoriaResponseDTO from(Categoria obj);

    @Mapping(target = "id", ignore = true)
    Categoria from(CategoriaRequestDTO dto);

}