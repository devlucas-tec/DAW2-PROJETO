package br.edu.ifpb.es.daw.rest;

import br.edu.ifpb.es.daw.exception.EntidadeNaoEncontradaException;
import br.edu.ifpb.es.daw.mapper.CarrinhoMapper;
import br.edu.ifpb.es.daw.model.Carrinho;
import br.edu.ifpb.es.daw.model.Cliente;
import br.edu.ifpb.es.daw.rest.dto.request.CarrinhoRequestDTO;
import br.edu.ifpb.es.daw.rest.dto.response.CarrinhoResponseDTO;
import br.edu.ifpb.es.daw.service.CarrinhoService;
import br.edu.ifpb.es.daw.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carrinhos")
public class CarrinhoRestController implements CarrinhoRestControllerApi {

    @Autowired private CarrinhoMapper mapper;
    @Autowired private CarrinhoService service;
    @Autowired private ClienteService clienteService;

    @Override @PostMapping
    public ResponseEntity<CarrinhoResponseDTO> adicionar(@RequestBody @Valid CarrinhoRequestDTO dto) {
        Carrinho obj = mapper.from(dto);
        Cliente cliente = clienteService.buscarPorId(dto.getIdCliente())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Cliente", dto.getIdCliente()));
        obj.setCliente(cliente);
        return new ResponseEntity<>(mapper.from(service.criar(obj)), HttpStatus.CREATED);
    }

    @Override @GetMapping("/{id}")
    public ResponseEntity<CarrinhoResponseDTO> recuperarPor(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.from(
                service.buscarPorId(id).orElseThrow(() -> new EntidadeNaoEncontradaException("Carrinho", id))));
    }

    @Override @DeleteMapping("/{id}/limpar")
    public ResponseEntity<CarrinhoResponseDTO> limpar(@PathVariable Long id) {
        Carrinho obj = service.buscarPorId(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Carrinho", id));
        return ResponseEntity.ok(mapper.from(service.limpar(obj)));
    }
}