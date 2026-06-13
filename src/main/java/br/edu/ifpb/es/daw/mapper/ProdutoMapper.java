package br.edu.ifpb.es.daw.mapper;

import br.edu.ifpb.es.daw.model.Produto;
import br.edu.ifpb.es.daw.rest.dto.request.ProdutoRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.ProdutoResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProdutoMapper {

    @Mapping(target = "nomeCategoria", source = "categoria.nome")
    @Mapping(target = "nomeVendedor", source = "vendedor.razaoSocial")
    ProdutoResponseDTO from(Produto obj);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "categoria", ignore = true)
    @Mapping(target = "vendedor", ignore = true)
    @Mapping(target = "avaliacoes", ignore = true)
    Produto from(ProdutoRequestDTO dto);

}