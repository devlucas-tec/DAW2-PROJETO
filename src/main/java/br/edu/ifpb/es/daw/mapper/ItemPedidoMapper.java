package br.edu.ifpb.es.daw.mapper;

import br.edu.ifpb.es.daw.model.ItemPedido;
import br.edu.ifpb.es.daw.rest.dto.response.ItemPedidoResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ItemPedidoMapper {

    @Mapping(target = "idPedido", source = "pedido.id")
    @Mapping(target = "idProduto", source = "produto.id")
    @Mapping(target = "nomeProduto", source = "produto.nome")
    ItemPedidoResponseDTO from(ItemPedido obj);
}
