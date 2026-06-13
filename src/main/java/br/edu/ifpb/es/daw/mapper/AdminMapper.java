package br.edu.ifpb.es.daw.mapper;

import br.edu.ifpb.es.daw.model.Admin;
import br.edu.ifpb.es.daw.rest.dto.request.AdminRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.AdminResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AdminMapper {

    AdminResponseDTO from(Admin obj);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "ativo", ignore = true)
    Admin from(AdminRequestDTO dto);

}