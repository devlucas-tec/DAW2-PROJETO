package br.edu.ifpb.es.daw.mapper;

import br.edu.ifpb.es.daw.model.ItemCarrinho;
import br.edu.ifpb.es.daw.rest.dto.response.ItemCarrinhoResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ItemCarrinhoMapper {

    @Mapping(target = "idCarrinho", source = "carrinho.id")
    @Mapping(target = "idProduto", source = "produto.id")
    @Mapping(target = "nomeProduto", source = "produto.nome")
    ItemCarrinhoResponseDTO from(ItemCarrinho obj);
}
