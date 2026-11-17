package br.edu.ifpb.es.daw.mapper;

import br.edu.ifpb.es.daw.model.Devolucao;
import br.edu.ifpb.es.daw.rest.dto.response.DevolucaoResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DevolucaoMapper {

    @Mapping(target = "idPedido", source = "pedido.id")
    @Mapping(target = "idCliente", source = "pedido.cliente.id")
    @Mapping(target = "nomeCliente", source = "pedido.cliente.nome")
    @Mapping(target = "valorPedido", source = "pedido.valorTotal")
    DevolucaoResponseDTO from(Devolucao obj);
}
