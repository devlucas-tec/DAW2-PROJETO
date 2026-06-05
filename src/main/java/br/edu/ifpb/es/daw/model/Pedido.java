package br.edu.ifpb.es.daw.model;

import br.edu.ifpb.es.daw.model.enums.StatusPedido;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id_pedido")
    private Long id;

    @Column(name = "data_pedido", nullable = false, updatable = false)
    private LocalDateTime dataPedido;

    @Column(name = "valor_total", precision = 10, scale = 2, nullable = false)
    private BigDecimal valorTotal;

    @Column(length = 50, nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusPedido status;

    // Relacionamento com Cliente
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    // Relacionamento bidirecional com os itens
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedido> itens = new ArrayList<>();

    // Relacionamento com Cupom
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cupom") // Um Pedido NÃO precisa obrigatoriamente de um Cupom
    private Cupom cupom;
}