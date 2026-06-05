package br.edu.ifpb.es.daw.mapper;

import br.edu.ifpb.es.daw.model.Pagamento;
import br.edu.ifpb.es.daw.rest.dto.request.PagamentoRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.PagamentoResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class PagamentoMapper {
    public PagamentoResponseDTO from(Pagamento obj) {
        PagamentoResponseDTO dto = new PagamentoResponseDTO();
        dto.setId(obj.getId());
        dto.setMetodo(obj.getMetodo());
        dto.setStatus(obj.getStatus());
        dto.setValorPago(obj.getValorPago());
        dto.setDataPagamento(obj.getDataPagamento());
        if (obj.getPedido() != null) dto.setIdPedido(obj.getPedido().getId());
        return dto;
    }
    public Pagamento from(PagamentoRequestDTO dto) {
        Pagamento obj = new Pagamento();
        obj.setMetodo(dto.getMetodo());
        obj.setValorPago(dto.getValorPago());
        return obj;
    }
}