package br.edu.ifpb.es.daw.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "pagamento")
public class Pagamento {

    @Id
    @GeneratedValue( strategy = GenerationType.SEQUENCE)
    @Column(name = "id_pagamento")
    private Long id;

    @Column(length = 50, nullable = false)
    private String metodo;

    @Column(length = 50, nullable = false)
    private String status;

    @Column(name = "valor_pago", precision = 10, scale = 2, nullable = false)
    private BigDecimal valorPago;

    @Column(name = "data_pagamento", insertable = false, updatable = false)
    private LocalDateTime dataPagamento;

    public Pagamento() {
    }

    @PrePersist
    protected void onCreate() {
        this.dataPagamento = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMetodo() {
        return metodo;
    }

    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getValorPago() {
        return valorPago;
    }

    public void setValorPago(BigDecimal valorPago) {
        this.valorPago = valorPago;
    }

    public LocalDateTime getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(LocalDateTime dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Pagamento pagamento = (Pagamento) o;
        return Objects.equals(id, pagamento.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Pagamento{" +
                "id=" + id +
                ", metodo='" + metodo + '\'' +
                ", valor=" + valorPago +
                ", status='" + status + '\'' +
                '}';
    }
}
