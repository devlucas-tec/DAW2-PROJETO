package br.edu.ifpb.es.daw.mapper;

import br.edu.ifpb.es.daw.model.Cupom;
import br.edu.ifpb.es.daw.rest.dto.request.CupomRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.CupomResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class CupomMapper {
    public CupomResponseDTO from(Cupom obj) {
        CupomResponseDTO dto = new CupomResponseDTO();
        dto.setId(obj.getId());
        dto.setCodigo(obj.getCodigo());
        dto.setValorDesconto(obj.getValorDesconto());
        dto.setDataExpiracao(obj.getDataExpiracao());
        dto.setStatus(obj.getStatus());
        return dto;
    }
    public Cupom from(CupomRequestDTO dto) {
        Cupom obj = new Cupom();
        obj.setCodigo(dto.getCodigo());
        obj.setValorDesconto(dto.getValorDesconto());
        obj.setDataExpiracao(dto.getDataExpiracao());
        return obj;
    }
}