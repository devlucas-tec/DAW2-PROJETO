package br.edu.ifpb.es.daw.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@Entity
@Table(name = "produto")
public class Produto {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id_produto")
    private Long id;

    @Column(length = 150, nullable = false)
    private String nome;

    @Column(length = 500)
    private String descricao;

    @Column(nullable = false)
    private Integer estoque;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    @Column(name = "imagem_url", length = 500)
    private String imagemUrl;

    @ToString.Exclude
    @ElementCollection
    @CollectionTable(name = "produto_imagem", joinColumns = @JoinColumn(name = "id_produto"))
    @OrderColumn(name = "ordem")
    @Column(name = "url", length = 500, nullable = false)
    private List<String> imagens = new ArrayList<>();

    @ColumnDefault("true")
    @Column(nullable = false)
    private boolean ativo = true;

    /** Média das notas com uma casa decimal. Fica 0 quando não há avaliações (o DTO devolve null nesse caso). */
    @ColumnDefault("0")
    @Column(name = "media_avaliacoes", nullable = false)
    private double mediaAvaliacoes;

    @ColumnDefault("0")
    @Column(name = "total_avaliacoes", nullable = false)
    private int totalAvaliacoes;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_vendedor", nullable = false)
    private Vendedor vendedor;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categoria", nullable = false)
    private Categoria categoria;

    @Column(name = "data_cadastro", nullable = false, updatable = false)
    private LocalDateTime dataCadastro;

    @Column(name = "data_atualizacao", nullable = false)
    private LocalDateTime dataAtualizacao;

    @ToString.Exclude
    @OneToMany(mappedBy = "produto")
    private List<Avaliacao> avaliacoes = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        if (this.dataCadastro == null) this.dataCadastro = LocalDateTime.now();
        this.dataAtualizacao = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.dataAtualizacao = LocalDateTime.now();
    }

    /** Produto que pode ser vendido: anúncio ativo de um vendedor ativo. */
    public boolean isDisponivelParaVenda() {
        return ativo && vendedor != null && vendedor.isAtivo();
    }
}
