package br.edu.ifpb.es.daw.mapper;

import br.edu.ifpb.es.daw.model.Devolucao;
import br.edu.ifpb.es.daw.rest.dto.request.DevolucaoRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.DevolucaoResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DevolucaoMapper {

    @Mapping(target = "idPedido", source = "pedido.id")
    DevolucaoResponseDTO from(Devolucao obj);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataDevolucao", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "pedido", ignore = true)
    Devolucao from(DevolucaoRequestDTO dto);

}