package br.edu.ifpb.es.daw.mapper;

import br.edu.ifpb.es.daw.model.Carrinho;
import br.edu.ifpb.es.daw.rest.dto.response.CarrinhoResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;

@Mapper(componentModel = "spring", uses = ItemCarrinhoMapper.class)
public interface CarrinhoMapper {

    @Mapping(target = "idCliente", source = "cliente.id")
    @Mapping(target = "nomeCliente", source = "cliente.nome")
    @Mapping(target = "valorTotal", expression = "java(calcularValorTotal(obj))")
    CarrinhoResponseDTO from(Carrinho obj);

    default BigDecimal calcularValorTotal(Carrinho carrinho) {
        return carrinho.getItens().stream()
                .map(item -> item.getPrecoUnitario().multiply(BigDecimal.valueOf(item.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
