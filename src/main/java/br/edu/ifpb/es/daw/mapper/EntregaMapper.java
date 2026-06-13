package br.edu.ifpb.es.daw.mapper;

import br.edu.ifpb.es.daw.model.Entrega;
import br.edu.ifpb.es.daw.rest.dto.request.EntregaRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.EntregaResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EntregaMapper {

    @Mapping(target = "idPedido", source = "pedido.id")
    EntregaResponseDTO from(Entrega obj);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataEnvio", ignore = true)
    @Mapping(target = "dataEntregaPrevista", ignore = true)
    @Mapping(target = "pedido", ignore = true)
    Entrega from(EntregaRequestDTO dto);

}