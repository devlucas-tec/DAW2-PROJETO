package br.edu.ifpb.es.daw.mapper;

import br.edu.ifpb.es.daw.model.Carrinho;
import br.edu.ifpb.es.daw.rest.dto.request.CarrinhoRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.CarrinhoResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class CarrinhoMapper {
    public CarrinhoResponseDTO from(Carrinho obj) {
        CarrinhoResponseDTO dto = new CarrinhoResponseDTO();
        dto.setId(obj.getId());
        dto.setDataCriacao(obj.getDataCriacao());
        dto.setDataAtualizacao(obj.getDataAtualizacao());
        if (obj.getCliente() != null) {
            dto.setIdCliente(obj.getCliente().getId());
            dto.setNomeCliente(obj.getCliente().getNome());
        }
        return dto;
    }
    public Carrinho from(CarrinhoRequestDTO dto) {
        return new Carrinho();
    }
}