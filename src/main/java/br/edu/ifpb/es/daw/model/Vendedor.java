package br.edu.ifpb.es.daw.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
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

}