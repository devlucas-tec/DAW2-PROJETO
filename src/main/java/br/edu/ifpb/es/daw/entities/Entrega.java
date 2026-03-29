package br.edu.ifpb.es.daw.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "entrega")
public class Entrega {

    @Id
    @GeneratedValue( strategy = GenerationType.SEQUENCE)
    @Column(name = "id_entrega")
    private Long id;

    @Column(length = 50)
    private String transportadora;

    @Column(length = 50, unique = true)
    private String codigoRastreamento;

    @Column(name = "status_entrega", nullable = false)
    private String statusEntrega;

    @Column(name = "data_envio")
    private LocalDateTime dataEnvio;

    @Column(name = "data_entrega_prevista")
    private LocalDateTime dataEntregaPrevista;

    public Entrega() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTransportadora() {
        return transportadora;
    }

    public void setTransportadora(String transportadora) {
        this.transportadora = transportadora;
    }

    public String getCodigoRastreamento() {
        return codigoRastreamento;
    }

    public void setCodigoRastreamento(String codigoRastreamento) {
        this.codigoRastreamento = codigoRastreamento;
    }

    public String getStatusEntrega() {
        return statusEntrega;
    }

    public void setStatusEntrega(String statusEntrega) {
        this.statusEntrega = statusEntrega;
    }

    public LocalDateTime getDataEnvio() {
        return dataEnvio;
    }

    public void setDataEnvio(LocalDateTime dataEnvio) {
        this.dataEnvio = dataEnvio;
    }

    public LocalDateTime getDataEntregaPrevista() {
        return dataEntregaPrevista;
    }

    public void setDataEntregaPrevista(LocalDateTime dataEntregaPrevista) {
        this.dataEntregaPrevista = dataEntregaPrevista;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Entrega entrega = (Entrega) o;
        return Objects.equals(id, entrega.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Entrega{" +
                "id=" + id +
                ", transportadora='" + transportadora + '\'' +
                ", rastreio='" + codigoRastreamento + '\'' +
                ", status='" + statusEntrega + '\'' +
                '}';
    }
}
