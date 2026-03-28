package br.edu.ifpb.es.daw.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "categoria")
public class Categoria {

    @Id
    @GeneratedValue( strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(length = 50, unique = true, nullable = false)
    private String nome;

    @Column(length = 350)
    private String descricao;


    // adicionar equals, hashcode e toString

}
