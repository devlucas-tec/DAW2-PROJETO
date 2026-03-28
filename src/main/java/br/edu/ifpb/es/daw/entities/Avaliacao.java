package br.edu.ifpb.es.daw.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table
public class Avaliacao {

    @Id
    @GeneratedValue( strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private Integer nota;

    @Column(length = 350)
    private String comentario;

    @Column(name = "data_avaliacao", insertable = false, updatable = false)
    private LocalDateTime dataAvaliacao;

    // adicionar equals, hashcode e toString

}
