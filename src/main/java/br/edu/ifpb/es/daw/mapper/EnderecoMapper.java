package br.edu.ifpb.es.daw.mapper;

import br.edu.ifpb.es.daw.model.Endereco;
import br.edu.ifpb.es.daw.rest.dto.request.EnderecoRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.EnderecoResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EnderecoMapper {

    @Mapping(target = "idCliente", source = "cliente.id")
    EnderecoResponseDTO from(Endereco obj);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cliente", ignore = true)
    Endereco from(EnderecoRequestDTO dto);

}