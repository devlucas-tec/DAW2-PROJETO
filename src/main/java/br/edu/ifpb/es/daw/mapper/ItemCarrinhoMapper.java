package br.edu.ifpb.es.daw.mapper;

import br.edu.ifpb.es.daw.model.ItemCarrinho;
import br.edu.ifpb.es.daw.rest.dto.request.ItemCarrinhoRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.ItemCarrinhoResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class ItemCarrinhoMapper {
    public ItemCarrinhoResponseDTO from(ItemCarrinho obj) {
        ItemCarrinhoResponseDTO dto = new ItemCarrinhoResponseDTO();
        dto.setQuantidade(obj.getQuantidade());
        dto.setPrecoUnitario(obj.getPrecoUnitario());
        if (obj.getCarrinho() != null) dto.setIdCarrinho(obj.getCarrinho().getId());
        if (obj.getProduto() != null) {
            dto.setIdProduto(obj.getProduto().getId());
            dto.setNomeProduto(obj.getProduto().getNome());
        }
        return dto;
    }
    public ItemCarrinho from(ItemCarrinhoRequestDTO dto) {
        ItemCarrinho obj = new ItemCarrinho();
        obj.setQuantidade(dto.getQuantidade());
        obj.setPrecoUnitario(dto.getPrecoUnitario());
        return obj;
    }
}