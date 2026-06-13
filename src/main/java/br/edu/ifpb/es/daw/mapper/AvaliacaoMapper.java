package br.edu.ifpb.es.daw.mapper;

import br.edu.ifpb.es.daw.model.Avaliacao;
import br.edu.ifpb.es.daw.rest.dto.request.AvaliacaoRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.AvaliacaoResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AvaliacaoMapper {

    @Mapping(target = "idCliente", source = "cliente.id")
    @Mapping(target = "nomeCliente", source = "cliente.nome")
    @Mapping(target = "idProduto", source = "produto.id")
    @Mapping(target = "nomeProduto", source = "produto.nome")
    AvaliacaoResponseDTO from(Avaliacao obj);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataAvaliacao", ignore = true)
    @Mapping(target = "cliente", ignore = true)
    @Mapping(target = "produto", ignore = true)
    Avaliacao from(AvaliacaoRequestDTO dto);

}