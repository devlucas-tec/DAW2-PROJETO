package br.edu.ifpb.es.daw.rest.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProdutoResponseDTO {
    private Long id;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private Integer estoque;
    private String imagemUrl;
    private List<String> imagens;
    private boolean ativo;
    /** Média com uma casa decimal; null quando o produto ainda não tem avaliações. */
    private Double mediaAvaliacoes;
    private int totalAvaliacoes;
    private Long idCategoria;
    private String nomeCategoria;
    private Long idVendedor;
    private String nomeVendedor;
    private LocalDateTime dataCadastro;
}
