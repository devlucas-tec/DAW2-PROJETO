package br.edu.ifpb.es.daw.mapper;

import br.edu.ifpb.es.daw.model.Pagamento;
import br.edu.ifpb.es.daw.rest.dto.request.PagamentoRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.PagamentoResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PagamentoMapper {

    @Mapping(target = "idPedido", source = "pedido.id")
    PagamentoResponseDTO from(Pagamento obj);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "dataPagamento", ignore = true)
    @Mapping(target = "pedido", ignore = true)
    Pagamento from(PagamentoRequestDTO dto);

}