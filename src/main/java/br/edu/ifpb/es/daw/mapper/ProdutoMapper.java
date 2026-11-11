package br.edu.ifpb.es.daw.mapper;

import br.edu.ifpb.es.daw.model.Produto;
import br.edu.ifpb.es.daw.rest.dto.request.ProdutoRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.ProdutoResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProdutoMapper {

    @Mapping(target = "idCategoria", source = "categoria.id")
    @Mapping(target = "nomeCategoria", source = "categoria.nome")
    @Mapping(target = "idVendedor", source = "vendedor.id")
    @Mapping(target = "nomeVendedor", source = "vendedor.razaoSocial")
    @Mapping(target = "mediaAvaliacoes", expression = "java(obj.getTotalAvaliacoes() > 0 ? obj.getMediaAvaliacoes() : null)")
    ProdutoResponseDTO from(Produto obj);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "categoria", ignore = true)
    @Mapping(target = "vendedor", ignore = true)
    @Mapping(target = "avaliacoes", ignore = true)
    @Mapping(target = "mediaAvaliacoes", ignore = true)
    @Mapping(target = "totalAvaliacoes", ignore = true)
    @Mapping(target = "imagens", expression = "java(dto.getImagens() != null ? new java.util.ArrayList<>(dto.getImagens()) : new java.util.ArrayList<>())")
    @Mapping(target = "ativo", expression = "java(dto.getAtivo() == null || dto.getAtivo())")
    Produto from(ProdutoRequestDTO dto);
}
