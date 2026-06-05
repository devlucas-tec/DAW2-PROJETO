package br.edu.ifpb.es.daw.mapper;

import br.edu.ifpb.es.daw.model.Entrega;
import br.edu.ifpb.es.daw.rest.dto.request.EntregaRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.EntregaResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class EntregaMapper {

    public EntregaResponseDTO from(Entrega obj) {
        EntregaResponseDTO dto = new EntregaResponseDTO();
        dto.setId(obj.getId());
        dto.setTransportadora(obj.getTransportadora());
        dto.setCodigoRastreamento(obj.getCodigoRastreamento());
        dto.setStatusEntrega(obj.getStatusEntrega());
        dto.setDataEnvio(obj.getDataEnvio());
        dto.setDataEntregaPrevista(obj.getDataEntregaPrevista());
        if (obj.getPedido() != null) dto.setIdPedido(obj.getPedido().getId());
        return dto;
    }

    public Entrega from(EntregaRequestDTO dto) {
        Entrega obj = new Entrega();
        obj.setTransportadora(dto.getTransportadora());
        obj.setCodigoRastreamento(dto.getCodigoRastreamento());
        obj.setStatusEntrega(dto.getStatusEntrega());
        return obj;
    }
}