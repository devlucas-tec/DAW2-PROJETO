package br.edu.ifpb.es.daw.mapper;

import br.edu.ifpb.es.daw.model.Cliente;
import br.edu.ifpb.es.daw.rest.dto.request.ClienteRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.ClienteResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClienteMapper {

    ClienteResponseDTO from(Cliente obj);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "pedidos", ignore = true)
    @Mapping(target = "carrinho", ignore = true)
    @Mapping(target = "avaliacoes", ignore = true)
    Cliente from(ClienteRequestDTO dto);

}