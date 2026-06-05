package br.edu.ifpb.es.daw.mapper;

import br.edu.ifpb.es.daw.model.Vendedor;
import br.edu.ifpb.es.daw.rest.dto.request.VendedorRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.VendedorResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class VendedorMapper {

    public VendedorResponseDTO from(Vendedor obj) {
        VendedorResponseDTO dto = new VendedorResponseDTO();
        dto.setId(obj.getId());
        dto.setRazaoSocial(obj.getRazaoSocial());
        dto.setCnpjCpf(obj.getCnpjCpf());
        return dto;
    }

    public Vendedor from(VendedorRequestDTO dto) {
        Vendedor obj = new Vendedor();
        obj.setRazaoSocial(dto.getRazaoSocial());
        obj.setCnpjCpf(dto.getCnpjCpf());
        return obj;
    }
}