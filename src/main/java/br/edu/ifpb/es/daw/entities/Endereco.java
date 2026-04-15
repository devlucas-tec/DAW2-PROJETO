package br.edu.ifpb.es.daw.entities;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "endereco")
public class Endereco {

    @Id
    @GeneratedValue( strategy = GenerationType.SEQUENCE)
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

    public Endereco() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Endereco endereco = (Endereco) o;
        return Objects.equals(id, endereco.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Endereco{" +
                "id=" + id +
                ", rua='" + rua + '\'' +
                ", cidade='" + cidade + '\'' +
                '}';
    }

}
