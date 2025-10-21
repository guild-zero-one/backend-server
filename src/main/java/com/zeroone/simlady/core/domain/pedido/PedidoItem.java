package com.zeroone.simlady.core.domain.pedido;

import com.zeroone.simlady.core.domain.pedido.pedidoVOs.Preco;
import com.zeroone.simlady.core.domain.pedido.pedidoVOs.Quantidade;

import java.util.UUID;

public class PedidoItem {
    private UUID id;
    private UUID idProduto;
    private Quantidade quantidade;
    private Preco precoUnitario;

    private PedidoItem(UUID id, UUID idProduto, Quantidade quantidade, Preco precoUnitario) {
        this.id = id;
        this.idProduto = idProduto;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
    }

    public static PedidoItem of(UUID id, UUID idProduto, Integer quantidade, String precoUnitario) {
        return new PedidoItem(
                id,
                idProduto,
                Quantidade.of(quantidade),
                Preco.of(precoUnitario)
        );
    }

    public static PedidoItem newPedidoItem(UUID idProduto, Integer quantidade, String precoUnitario) {
        return PedidoItem.of(
                UUID.randomUUID(),
                idProduto,
                quantidade,
                precoUnitario
        );
    }

    public Preco calcularSubtotal() {
        return precoUnitario.multiplicar(quantidade);
    }

    public UUID getId() {
        return id;
    }

    public UUID getIdProduto() {
        return idProduto;
    }

    public Quantidade getQuantidade() {
        return quantidade;
    }

    public Preco getPrecoUnitario() {
        return precoUnitario;
    }
}
