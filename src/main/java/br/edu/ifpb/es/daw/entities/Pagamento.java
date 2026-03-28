package br.edu.ifpb.es.daw.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagamento")
public class Pagamento {

    @Id
    @GeneratedValue( strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(length = 50, nullable = false)
    private String metodo;

    @Column(length = 50, nullable = false)
    private String status;

    @Column(name = "valor_pago", precision = 10, scale = 2, nullable = false)
    private BigDecimal valorPago;

    @Column(name = "data_pagamento", insertable = false, updatable = false)
    private LocalDateTime dataPagamento;


    // adicionar equals, hashcode e toString

}
