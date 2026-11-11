package br.edu.ifpb.es.daw.mapper;

import br.edu.ifpb.es.daw.model.Compra;
import br.edu.ifpb.es.daw.rest.dto.response.CompraResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = PedidoMapper.class)
public interface CompraMapper {

    @Mapping(target = "idCliente", source = "cliente.id")
    @Mapping(target = "nomeCliente", source = "cliente.nome")
    @Mapping(target = "codigoCupom", source = "cupom.codigo")
    @Mapping(target = "quantidadePedidos", expression = "java(obj.getPedidos().size())")
    CompraResponseDTO from(Compra obj);
}
