package br.edu.ifpb.es.daw.model;

import br.edu.ifpb.es.daw.model.enums.StatusCupom;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "cupom")
public class Cupom {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(length = 20, unique = true, nullable = false)
    private String codigo;

    @Column(name = "valor_desconto", precision = 10, scale = 2, nullable = false)
    private BigDecimal valorDesconto;

    @Column(name = "data_expiracao")
    private LocalDate dataExpiracao;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private StatusCupom status = StatusCupom.ATIVO;

    @OneToMany(mappedBy = "cupom")
    private List<Pedido> pedidos = new ArrayList<>();
}