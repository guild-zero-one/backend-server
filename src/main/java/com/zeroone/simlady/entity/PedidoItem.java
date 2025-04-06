package com.zeroone.simlady.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter

public class PedidoItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "fk_lote_produto", nullable = false)
    private LoteProduto loteProduto;

    @ManyToOne
    @JoinColumn(name = "fk_pedido", nullable = false)
    private PedidoVenda pedidoVenda;

    private Integer quantidade;
    private BigDecimal precoUnitario;



}
