package br.edu.ifpb.es.daw.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "entrega")
public class Entrega {

    @Id
    @GeneratedValue( strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(length = 50)
    private String transportadora;

    @Column(length = 50)
    private String codigoRastreamento;

    @Column(name = "status_entrega", nullable = false)
    private String statusEntrega;

    @Column(name = "data_envio")
    private LocalDateTime dataEnvio;

    @Column(name = "data_entrega_prevista")
    private LocalDateTime dataEntregaPrevista;


    // adicionar equals, hashcode e toString

}
