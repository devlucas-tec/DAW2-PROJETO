package br.edu.ifpb.es.daw.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "cupom")
public class Cupom {

    @Id
    @GeneratedValue( strategy = GenerationType.SEQUENCE)
    @Column(name = "id_cupom")
    private Long id;

    @Column(length = 20, unique = true, nullable = false)
    private String codigo;

    @Column(name = "valor_desconto", precision = 10, scale = 2, nullable = false)
    private BigDecimal valorDesconto;

    @Column(name = "data_expiracao")
    private LocalDate dataExpiracao;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private StatusEnum status = StatusEnum.ATIVO;

    public Cupom() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public BigDecimal getValorDesconto() {
        return valorDesconto;
    }

    public void setValorDesconto(BigDecimal valorDesconto) {
        this.valorDesconto = valorDesconto;
    }

    public LocalDate getDataExpiracao() {
        return dataExpiracao;
    }

    public void setDataExpiracao(LocalDate dataExpiracao) {
        this.dataExpiracao = dataExpiracao;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cupom cupom = (Cupom) o;
        return Objects.equals(codigo, cupom.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }

    @Override
    public String toString() {
        return "Cupom{" +
                "id=" + id +
                ", codigo='" + codigo + '\'' +
                ", status=" + status +
                '}';
    }

}
