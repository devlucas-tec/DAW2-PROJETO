package br.edu.ifpb.es.daw.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pedido")
public class Pedido {

    @Id
    @GeneratedValue( strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "data_pedido", insertable = false, updatable = false)
    private LocalDateTime dataPedido;

    //max de 10 digitos e so 2 apos a virgula
    @Column(, name = "valor_total", precision = 10, scale = 2, nullable = false)
    private BigDecimal valorTotal;

    @Column(length = 50, nullable = false)
    private String status;

    // adicionar equals, hashcode e toString

}
