package br.edu.ifpb.es.daw.mapper;

import br.edu.ifpb.es.daw.model.Vendedor;
import br.edu.ifpb.es.daw.rest.dto.request.VendedorRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.VendedorResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VendedorMapper {

    VendedorResponseDTO from(Vendedor obj);

    @Mapping(target = "id", ignore = true)
    Vendedor from(VendedorRequestDTO dto);

}