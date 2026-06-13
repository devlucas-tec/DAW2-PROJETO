package br.edu.ifpb.es.daw.mapper;

import br.edu.ifpb.es.daw.model.Cupom;
import br.edu.ifpb.es.daw.rest.dto.request.CupomRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.CupomResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CupomMapper {

    CupomResponseDTO from(Cupom obj);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "pedidos", ignore = true)
    Cupom from(CupomRequestDTO dto);

}