package br.edu.ifpb.es.daw.mapper;

import br.edu.ifpb.es.daw.model.Pedido;
import br.edu.ifpb.es.daw.rest.dto.request.PedidoRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.PedidoResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class PedidoMapper {

    public PedidoResponseDTO from(Pedido obj) {
        PedidoResponseDTO dto = new PedidoResponseDTO();
        dto.setId(obj.getId());
        dto.setDataPedido(obj.getDataPedido());
        dto.setValorTotal(obj.getValorTotal());
        dto.setStatus(obj.getStatus());
        if (obj.getCliente() != null) {
            dto.setIdCliente(obj.getCliente().getId());
            dto.setNomeCliente(obj.getCliente().getNome());
        }
        return dto;
    }

    public Pedido from(PedidoRequestDTO dto) {
        Pedido obj = new Pedido();
        obj.setValorTotal(dto.getValorTotal());
        return obj;
    }
}