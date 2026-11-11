package br.edu.ifpb.es.daw.rest.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ItemCarrinhoResponseDTO {
    private Long idCarrinho;
    private Long idProduto;
    private String nomeProduto;
    private String imagemUrl;
    private Long idVendedor;
    private String nomeVendedor;
    private Integer quantidade;
    private BigDecimal precoUnitario;
    private BigDecimal subtotal;
    private Integer estoqueDisponivel;
    /** false quando o anúncio foi pausado ou o vendedor bloqueado; o item precisa ser removido antes do checkout. */
    private boolean disponivel;
}
