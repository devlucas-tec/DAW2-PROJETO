package br.edu.ifpb.es.daw.mapper;
import br.edu.ifpb.es.daw.model.Devolucao;
import br.edu.ifpb.es.daw.rest.dto.request.DevolucaoRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.DevolucaoResponseDTO;
import org.springframework.stereotype.Component;
@Component
public class DevolucaoMapper {
    public DevolucaoResponseDTO from(Devolucao obj) {
        DevolucaoResponseDTO dto = new DevolucaoResponseDTO();
        dto.setId(obj.getId());
        dto.setMotivo(obj.getMotivo());
        dto.setStatus(obj.getStatus());
        dto.setDataDevolucao(obj.getDataDevolucao());
        if (obj.getPedido() != null) dto.setIdPedido(obj.getPedido().getId());
        return dto;
    }
    public Devolucao from(DevolucaoRequestDTO dto) {
        Devolucao obj = new Devolucao();
        obj.setMotivo(dto.getMotivo());
        return obj;
    }
}