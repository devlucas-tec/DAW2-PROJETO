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
@Table(name = "vendedor")
public class Vendedor {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id_vendedor")
    private Long id;

    @Column(name = "razao_social", length = 150, nullable = false)
    private String razaoSocial;

    @Column(name = "cnj_cpf", length = 18, nullable = false, unique = true)
    private String cnpjCpf;

}