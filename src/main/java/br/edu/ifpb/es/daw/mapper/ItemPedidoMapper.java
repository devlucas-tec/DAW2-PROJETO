package br.edu.ifpb.es.daw.mapper;

import br.edu.ifpb.es.daw.model.ItemPedido;
import br.edu.ifpb.es.daw.rest.dto.request.ItemPedidoRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.ItemPedidoResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class ItemPedidoMapper {
    public ItemPedidoResponseDTO from(ItemPedido obj) {
        ItemPedidoResponseDTO dto = new ItemPedidoResponseDTO();
        dto.setQuantidade(obj.getQuantidade());
        dto.setPrecoUnitario(obj.getPrecoUnitario());
        if (obj.getPedido() != null) dto.setIdPedido(obj.getPedido().getId());
        if (obj.getProduto() != null) {
            dto.setIdProduto(obj.getProduto().getId());
            dto.setNomeProduto(obj.getProduto().getNome());
        }
        return dto;
    }
    public ItemPedido from(ItemPedidoRequestDTO dto) {
        ItemPedido obj = new ItemPedido();
        obj.setQuantidade(dto.getQuantidade());
        obj.setPrecoUnitario(dto.getPrecoUnitario());
        return obj;
    }
}