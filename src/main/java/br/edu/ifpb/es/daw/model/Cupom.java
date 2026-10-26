package br.edu.ifpb.es.daw.model;

import br.edu.ifpb.es.daw.model.enums.StatusCupom;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@Entity
@Table(name = "cupom")
public class Cupom {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(length = 20, unique = true, nullable = false)
    private String codigo;

    @Column(name = "valor_desconto", precision = 10, scale = 2, nullable = false)
    private BigDecimal valorDesconto;

    @Column(name = "data_expiracao")
    private LocalDate dataExpiracao;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private StatusCupom status = StatusCupom.ATIVO;

    @ToString.Exclude
    @OneToMany(mappedBy = "cupom")
    private List<Pedido> pedidos = new ArrayList<>();

    public boolean isValido() {
        return status == StatusCupom.ATIVO
                && (dataExpiracao == null || !dataExpiracao.isBefore(LocalDate.now()));
    }
}
