package br.edu.ifpb.es.daw.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;
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

    @Column(name = "tipo_compensacao", length = 50, nullable = false)
    private String tipoCompensacao;

    @Column(name = "valor_compensado", precision = 10, scale = 2)
    private BigDecimal valorCompensado;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private StatusEnum status = StatusEnum.ATIVO;

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

    public String getTipoCompensacao() {
        return tipoCompensacao;
    }

    public void setTipoCompensacao(String tipoCompensacao) {
        this.tipoCompensacao = tipoCompensacao;
    }

    public BigDecimal getValorCompensado() {
        return valorCompensado;
    }

    public void setValorCompensado(BigDecimal valorCompensado) {
        this.valorCompensado = valorCompensado;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = StatusEnum.valueOf(status);
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
                ", valorCompensado=" + valorCompensado +
                '}';
    }

}
