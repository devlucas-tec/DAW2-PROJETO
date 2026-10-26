package br.edu.ifpb.es.daw.mapper;

import br.edu.ifpb.es.daw.model.Entrega;
import br.edu.ifpb.es.daw.rest.dto.response.EntregaResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EntregaMapper {

    @Mapping(target = "idPedido", source = "pedido.id")
    EntregaResponseDTO from(Entrega obj);
}
