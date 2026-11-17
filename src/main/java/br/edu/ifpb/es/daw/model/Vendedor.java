package br.edu.ifpb.es.daw.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@NoArgsConstructor
@Entity
@Table(name = "vendedor")
@PrimaryKeyJoinColumn(name = "id")
@DiscriminatorValue("VENDEDOR")
public class Vendedor extends Usuario {

    @Column(name = "razao_social", length = 150, nullable = false)
    private String razaoSocial;

    @Column(name = "cnpj_cpf", length = 18, nullable = false, unique = true)
    private String cnpjCpf;

    /** Descrição breve da loja, exibida no perfil público. */
    @Column(length = 500)
    private String descricao;

    @Column(name = "banner_url", length = 500)
    private String bannerUrl;

    /** Loja fechada temporariamente pelo vendedor: os produtos saem da vitrine, mas a conta continua ativa. */
    @org.hibernate.annotations.ColumnDefault("true")
    @Column(name = "loja_aberta", nullable = false)
    private boolean lojaAberta = true;

    /** A loja aparece para os clientes: vendedor ativo e loja aberta. */
    public boolean isLojaVisivel() {
        return isAtivo() && lojaAberta;
    }
}
