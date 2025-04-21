package com.zeroone.simlady.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Table(name = "pedido_item")
public class PedidoItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "fk_produto", nullable = false)
    private Produto produto;

    @ManyToOne
    @JoinColumn(name = "fk_pedido", nullable = false)
    private PedidoVenda pedidoVenda;

    private Integer quantidade;
    private BigDecimal precoUnitario;
}
