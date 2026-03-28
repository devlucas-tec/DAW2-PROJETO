package br.edu.ifpb.es.daw.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "devolucao")
public class Devolucao {

    @Id
    @GeneratedValue( strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "data_devolucao", insertable = false, updatable = false)
    private LocalDateTime dataDevolucao;

    @Column(nullable = false)
    private String motivo;

    @Column(name = "tipo_compensacao", length = 50, nullable = false)
    private String tipoCompensacao;

    @Column(name = "valor_compensado", precision = 10, scale = 2)
    private BigDecimal valorCompensado;

    @Column(length = 50, nullable = false)
    private String status;

    // adicionar equals, hashcode e toString

}
