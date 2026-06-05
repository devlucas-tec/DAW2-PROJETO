package br.edu.ifpb.es.daw.mapper;

import br.edu.ifpb.es.daw.model.Endereco;
import br.edu.ifpb.es.daw.rest.dto.request.EnderecoRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.EnderecoResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class EnderecoMapper {
    public EnderecoResponseDTO from(Endereco obj) {
        EnderecoResponseDTO dto = new EnderecoResponseDTO();
        dto.setId(obj.getId());
        dto.setRua(obj.getRua());
        dto.setNumero(obj.getNumero());
        dto.setComplemento(obj.getComplemento());
        dto.setCep(obj.getCep());
        dto.setCidade(obj.getCidade());
        dto.setEstado(obj.getEstado());
        return dto;
    }
    public Endereco from(EnderecoRequestDTO dto) {
        Endereco obj = new Endereco();
        obj.setRua(dto.getRua());
        obj.setNumero(dto.getNumero());
        obj.setComplemento(dto.getComplemento());
        obj.setCep(dto.getCep());
        obj.setCidade(dto.getCidade());
        obj.setEstado(dto.getEstado());
        return obj;
    }
}