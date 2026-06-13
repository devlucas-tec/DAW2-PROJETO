package br.edu.ifpb.es.daw.model;

import jakarta.persistence.Entity;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = "admin")
@PrimaryKeyJoinColumn(name = "id")
@DiscriminatorValue("ADMIN")
public class Admin extends Usuario {
    // sem campos adicionais por enquanto
}