package br.edu.ifpb.es.daw.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "devolucao")
public class Devolucao {

    @Id
    @GeneratedValue( strategy = GenerationType.SEQUENCE)
    @Column(name = "id_devolucao")
    private Long id;

    @Column(name = "data_devolucao", insertable = false, updatable = false)
    private LocalDateTime dataDevolucao;

    @Column(nullable = false, length = 1000)
    private String motivo;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private StatusDevolucao status = StatusDevolucao.APROVADA;

    public Devolucao() {
    }

    @PrePersist
    protected void onCreate() {
        this.dataDevolucao = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDataDevolucao() {
        return dataDevolucao;
    }

    public void setDataDevolucao(LocalDateTime dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public StatusDevolucao getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = StatusDevolucao.valueOf(status);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Devolucao devolucao = (Devolucao) o;
        return Objects.equals(id, devolucao.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Devolucao{" +
                "id=" + id +
                ", status='" + status + '\'' +
                '}';
    }

}
