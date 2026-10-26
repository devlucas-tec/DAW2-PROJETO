package br.edu.ifpb.es.daw.mapper;

import br.edu.ifpb.es.daw.model.Pagamento;
import br.edu.ifpb.es.daw.rest.dto.response.PagamentoResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PagamentoMapper {

    @Mapping(target = "idPedido", source = "pedido.id")
    PagamentoResponseDTO from(Pagamento obj);
}
