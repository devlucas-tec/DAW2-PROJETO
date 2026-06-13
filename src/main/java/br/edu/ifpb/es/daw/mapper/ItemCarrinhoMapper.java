package br.edu.ifpb.es.daw.mapper;

import br.edu.ifpb.es.daw.model.ItemCarrinho;
import br.edu.ifpb.es.daw.rest.dto.request.ItemCarrinhoRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.ItemCarrinhoResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ItemCarrinhoMapper {

    @Mapping(target = "idCarrinho", source = "carrinho.id")
    @Mapping(target = "idProduto", source = "produto.id")
    @Mapping(target = "nomeProduto", source = "produto.nome")
    ItemCarrinhoResponseDTO from(ItemCarrinho obj);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "carrinho", ignore = true)
    @Mapping(target = "produto", ignore = true)
    ItemCarrinho from(ItemCarrinhoRequestDTO dto);

}