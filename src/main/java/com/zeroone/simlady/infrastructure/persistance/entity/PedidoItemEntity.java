package com.zeroone.simlady.infrastructure.persistance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "pedido_item")
public class PedidoItemEntity {
    @Id
    private UUID id;
    
    @Column(name = "produto_id", nullable = false)
    private UUID idProduto;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false)
    private PedidoEntity pedido;
    
    private Integer quantidade;
    
    @Column(name = "preco_unitario", precision = 10, scale = 2)
    private BigDecimal precoUnitario;

    public PedidoItemEntity(UUID id, UUID idProduto, PedidoEntity pedido, Integer quantidade, BigDecimal precoUnitario) {
        this.id = id;
        this.idProduto = idProduto;
        this.pedido = pedido;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
    }
}
