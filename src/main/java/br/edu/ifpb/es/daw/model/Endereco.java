package br.edu.ifpb.es.daw.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "endereco")
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(length = 100, nullable = false)
    private String rua;

    @Column(length = 10, nullable = false)
    private String numero;

    @Column(length = 50)
    private String complemento;

    @Column(length = 10, nullable = false)
    private String cep;

    @Column(length = 50, nullable = false)
    private String cidade;

    @Column(length = 2, nullable = false)
    private String estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;
}