package br.edu.ifpb.es.daw.mapper;

import br.edu.ifpb.es.daw.model.Pedido;
import br.edu.ifpb.es.daw.rest.dto.request.PedidoRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.PedidoResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PedidoMapper {

    @Mapping(target = "idCliente", source = "cliente.id")
    @Mapping(target = "nomeCliente", source = "cliente.nome")
    PedidoResponseDTO from(Pedido obj);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataPedido", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "cliente", ignore = true)
    @Mapping(target = "cupom", ignore = true)
    @Mapping(target = "itens", ignore = true)
    Pedido from(PedidoRequestDTO dto);

}