package com.zeroone.simlady.core.domain.pedido;

import com.zeroone.simlady.core.domain.pedido.pedidoVOs.Preco;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Pedido {
    private UUID id;
    private StatusPedido status;
    private UUID idVenda;
    private UUID idUsuario;
    private List<PedidoItem> itens;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

    private Pedido(UUID id, StatusPedido status, UUID idVenda, UUID idUsuario, 
                   List<PedidoItem> itens, LocalDateTime criadoEm, LocalDateTime atualizadoEm) {
        this.id = id;
        this.status = status;
        this.idVenda = idVenda;
        this.idUsuario = idUsuario;
        this.itens = itens != null ? new ArrayList<>(itens) : new ArrayList<>();
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
    }

    public static Pedido of(UUID id, StatusPedido status, UUID idVenda, UUID idUsuario, 
                           List<PedidoItem> itens, LocalDateTime criadoEm, LocalDateTime atualizadoEm) {
        return new Pedido(id, status, idVenda, idUsuario, itens, criadoEm, atualizadoEm);
    }

    public static Pedido newPedido(UUID idVenda, UUID idUsuario) {
        return Pedido.of(
                UUID.randomUUID(),
                StatusPedido.PENDENTE,
                idVenda,
                idUsuario,
                new ArrayList<>(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    public void adicionarItem(PedidoItem item) {
        if (item == null) {
            throw new IllegalArgumentException("Item não pode ser nulo");
        }
        this.itens.add(item);
        this.atualizadoEm = LocalDateTime.now();
    }

    public void removerItem(UUID itemId) {
        this.itens.removeIf(item -> item.getId().equals(itemId));
        this.atualizadoEm = LocalDateTime.now();
    }

    public Preco calcularTotal() {
        return itens.stream()
                .map(PedidoItem::calcularSubtotal)
                .reduce(Preco.of("0"), Preco::somar);
    }

    public void alterarStatus(StatusPedido novoStatus) {
        if (novoStatus == null) {
            throw new IllegalArgumentException("Status não pode ser nulo");
        }
        this.status = novoStatus;
        this.atualizadoEm = LocalDateTime.now();
    }

    public boolean podeSerCancelado() {
        return status == StatusPedido.PENDENTE;
    }

    public boolean podeSerConcluido() {
        return status == StatusPedido.PENDENTE && !itens.isEmpty();
    }

    public UUID getId() {
        return id;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public UUID getIdVenda() {
        return idVenda;
    }

    public UUID getIdUsuario() {
        return idUsuario;
    }

    public List<PedidoItem> getItens() {
        return new ArrayList<>(itens);
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }
}
