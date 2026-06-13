package br.edu.ifpb.es.daw.mapper;

import br.edu.ifpb.es.daw.model.Carrinho;
import br.edu.ifpb.es.daw.rest.dto.request.CarrinhoRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.CarrinhoResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CarrinhoMapper {

    @Mapping(target = "idCliente", source = "cliente.id")
    @Mapping(target = "nomeCliente", source = "cliente.nome")
    @Mapping(target = "itens", ignore = true)
    CarrinhoResponseDTO from(Carrinho obj);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "cliente", ignore = true)
    @Mapping(target = "itens", ignore = true)
    Carrinho from(CarrinhoRequestDTO dto);

}