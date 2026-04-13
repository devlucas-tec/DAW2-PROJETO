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
    @Enumerated(EnumType.STRING)
    private MetodoPagamento metodo;

    @Column(length = 50, nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusPagamento status;

    @Column(name = "valor_pago", precision = 10, scale = 2, nullable = false)
    private BigDecimal valorPago;

    @Column(name = "data_pagamento", nullable = false)
    private LocalDateTime dataPagamento;

    @PrePersist
    protected void onCreate() {
        this.dataPagamento = LocalDateTime.now();
    }

    public Pagamento() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MetodoPagamento getMetodo() {
        return metodo;
    }

    public void setMetodo(MetodoPagamento metodo) {
        this.metodo = metodo;
    }

    public StatusPagamento getStatus() {
        return status;
    }

    public void setStatus(StatusPagamento status) {
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
