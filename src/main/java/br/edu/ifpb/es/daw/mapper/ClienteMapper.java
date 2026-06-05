package br.edu.ifpb.es.daw.mapper;

import br.edu.ifpb.es.daw.model.Cliente;
import br.edu.ifpb.es.daw.rest.dto.request.ClienteRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.ClienteResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class ClienteMapper {

    public ClienteResponseDTO from(Cliente obj) {
        ClienteResponseDTO dto = new ClienteResponseDTO();
        dto.setId(obj.getId());
        dto.setNome(obj.getNome());
        dto.setEmail(obj.getEmail());
        dto.setTelefone(obj.getTelefone());
        dto.setDataCadastro(obj.getDataCadastro());
        return dto;
    }

    public Cliente from(ClienteRequestDTO dto) {
        Cliente obj = new Cliente();
        obj.setNome(dto.getNome());
        obj.setEmail(dto.getEmail());
        obj.setSenha(dto.getSenha());
        obj.setTelefone(dto.getTelefone());
        return obj;
    }
}