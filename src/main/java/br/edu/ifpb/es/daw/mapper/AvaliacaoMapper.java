package br.edu.ifpb.es.daw.mapper;

import br.edu.ifpb.es.daw.model.Avaliacao;
import br.edu.ifpb.es.daw.rest.dto.request.AvaliacaoRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.AvaliacaoResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class AvaliacaoMapper {

    public AvaliacaoResponseDTO from(Avaliacao obj) {
        AvaliacaoResponseDTO dto = new AvaliacaoResponseDTO();
        dto.setId(obj.getId());
        dto.setNota(obj.getNota());
        dto.setComentario(obj.getComentario());
        dto.setDataAvaliacao(obj.getDataAvaliacao());
        if (obj.getCliente() != null) {
            dto.setIdCliente(obj.getCliente().getId());
            dto.setNomeCliente(obj.getCliente().getNome());
        }
        if (obj.getProduto() != null) {
            dto.setIdProduto(obj.getProduto().getId());
            dto.setNomeProduto(obj.getProduto().getNome());
        }
        return dto;
    }

    public Avaliacao from(AvaliacaoRequestDTO dto) {
        Avaliacao obj = new Avaliacao();
        obj.setNota(dto.getNota());
        obj.setComentario(dto.getComentario());
        return obj;
    }
}