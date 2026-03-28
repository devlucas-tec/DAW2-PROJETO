package br.edu.ifpb.es.daw.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "cupom")
public class Cupom {

    @Id
    @GeneratedValue( strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(length = 20, unique = true, nullable = false)
    private String codigo;

    @Column(name = "valor_desconto", precision = 10, scale = 2, nullable = false)
    private BigDecimal valorDesconto;

    @Column(name = "data_expiracao")
    private LocalDate dataExpiracao;


    public enum StatusEnum {
        ATIVO,
        INATIVO,
        PENDENTE
    }
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private StatusEnum status = StatusEnum.ATIVO;

    // adicionar equals, hashcode e toString

}
