package br.edu.ifpb.es.daw.rest.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

/** Perfil público da loja: sem e-mail e sem CNPJ/CPF. */
@Data
public class VendedorPublicoResponseDTO {
    private Long id;
    private String nome;
    private String razaoSocial;
    private LocalDateTime dataCadastro;
    /** Descrição breve da loja escrita pelo vendedor. */
    private String descricao;
    private String bannerUrl;
    /** false quando o vendedor fechou a loja temporariamente (os produtos não aparecem na vitrine). */
    private boolean lojaAberta;
    private long totalProdutos;
    /** Média das avaliações dos produtos da loja (uma casa decimal); null se não houver avaliações. */
    private Double mediaAvaliacoes;
    private long totalAvaliacoes;
}
