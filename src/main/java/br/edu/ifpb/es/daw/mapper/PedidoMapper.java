package br.edu.ifpb.es.daw.mapper;

import br.edu.ifpb.es.daw.model.Pedido;
import br.edu.ifpb.es.daw.rest.dto.response.PedidoResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = ItemPedidoMapper.class)
public interface PedidoMapper {

    @Mapping(target = "idCliente", source = "cliente.id")
    @Mapping(target = "nomeCliente", source = "cliente.nome")
    @Mapping(target = "idCupom", source = "cupom.id")
    @Mapping(target = "codigoCupom", source = "cupom.codigo")
    @Mapping(target = "valorDesconto", source = "cupom.valorDesconto")
    PedidoResponseDTO from(Pedido obj);
}
