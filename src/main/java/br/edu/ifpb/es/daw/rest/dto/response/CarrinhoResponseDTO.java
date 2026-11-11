package br.edu.ifpb.es.daw.rest.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class CarrinhoResponseDTO {
    private Long id;
    private LocalDate dataCriacao;
    private LocalDate dataAtualizacao;
    private Long idCliente;
    private String nomeCliente;
    private BigDecimal valorTotal;
    /** Soma das quantidades de todos os itens. */
    private int quantidadeItens;
    private List<ItemCarrinhoResponseDTO> itens;
}
