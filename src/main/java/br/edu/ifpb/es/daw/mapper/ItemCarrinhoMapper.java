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
    @Mapping(target = "imagemUrl", source = "produto.imagemUrl")
    @Mapping(target = "idVendedor", source = "produto.vendedor.id")
    @Mapping(target = "nomeVendedor", source = "produto.vendedor.razaoSocial")
    @Mapping(target = "estoqueDisponivel", source = "produto.estoque")
    @Mapping(target = "disponivel", expression = "java(obj.getProduto().isDisponivelParaVenda())")
    @Mapping(target = "subtotal", expression = "java(obj.getPrecoUnitario().multiply(java.math.BigDecimal.valueOf(obj.getQuantidade())))")
    ItemCarrinhoResponseDTO from(ItemCarrinho obj);
}
