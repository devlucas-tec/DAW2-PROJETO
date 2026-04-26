package br.edu.ifpb.es.daw.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "entrega")
public class Entrega {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(length = 50)
    private String transportadora;

    @Column(name = "codigo_rastreamento", length = 50, unique = true)
    private String codigoRastreamento;

    @Column(name = "status_entrega", nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusEntrega statusEntrega;

    @Column(name = "data_envio")
    private LocalDateTime dataEnvio;

    @Column(name = "data_entrega_prevista")
    private LocalDateTime dataEntregaPrevista;

    @OneToOne
    @JoinColumn(name = "id_pedido")
    private Pedido pedido;

    @PrePersist
    protected void onCreate() {
        this.dataEnvio = LocalDateTime.now();
        this.dataEntregaPrevista = LocalDateTime.now().plusDays(7);
    }

    @PreUpdate
    protected void onUpdate() {
        this.dataEntregaPrevista = LocalDateTime.now().plusDays(7);
    }

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

    public StatusEntrega getStatusEntrega() {
        return statusEntrega;
    }

    public void setStatusEntrega(StatusEntrega statusEntrega) {
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

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
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