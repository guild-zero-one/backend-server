package com.zeroone.simlady.core.domain.venda;

import com.zeroone.simlady.core.domain.venda.vendaVOs.Desconto;
import com.zeroone.simlady.core.domain.venda.vendaVOs.ValorTotal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Venda {
    private UUID id;
    private ValorTotal valorTotal;
    private Desconto desconto;
    private Boolean pagamentoRealizado;
    private LocalDate dataVenda;
    private List<UUID> pedidosIds;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

    private Venda(UUID id, ValorTotal valorTotal, Desconto desconto, 
                  Boolean pagamentoRealizado, LocalDate dataVenda, List<UUID> pedidosIds,
                  LocalDateTime criadoEm, LocalDateTime atualizadoEm) {
        this.id = id;
        this.valorTotal = valorTotal;
        this.desconto = desconto;
        this.pagamentoRealizado = pagamentoRealizado;
        this.dataVenda = dataVenda;
        this.pedidosIds = pedidosIds != null ? new ArrayList<>(pedidosIds) : new ArrayList<>();
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
    }

    public static Venda of(UUID id, ValorTotal valorTotal, Desconto desconto,
                          Boolean pagamentoRealizado, LocalDate dataVenda, List<UUID> pedidosIds,
                          LocalDateTime criadoEm, LocalDateTime atualizadoEm) {
        return new Venda(id, valorTotal, desconto, pagamentoRealizado, dataVenda, 
                        pedidosIds, criadoEm, atualizadoEm);
    }

    public static Venda newVenda(ValorTotal valorTotal, Desconto desconto, LocalDate dataVenda) {
        return Venda.of(
                UUID.randomUUID(),
                valorTotal,
                desconto,
                false,
                dataVenda,
                new ArrayList<>(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    public void adicionarPedido(UUID pedidoId) {
        if (pedidoId == null) {
            throw new IllegalArgumentException("ID do pedido não pode ser nulo");
        }
        if (!this.pedidosIds.contains(pedidoId)) {
            this.pedidosIds.add(pedidoId);
            this.atualizadoEm = LocalDateTime.now();
        }
    }

    public void removerPedido(UUID pedidoId) {
        if (this.pedidosIds.remove(pedidoId)) {
            this.atualizadoEm = LocalDateTime.now();
        }
    }

    public void confirmarPagamento() {
        this.pagamentoRealizado = true;
        this.atualizadoEm = LocalDateTime.now();
    }

    public void cancelarPagamento() {
        this.pagamentoRealizado = false;
        this.atualizadoEm = LocalDateTime.now();
    }

    public ValorTotal calcularValorFinal() {
        if (desconto.isZero()) {
            return valorTotal;
        }
        return valorTotal.subtrair(ValorTotal.of(desconto.getValor()));
    }

    public boolean podeSerFinalizada() {
        return pagamentoRealizado;
    }

    public UUID getId() {
        return id;
    }

    public ValorTotal getValorTotal() {
        return valorTotal;
    }

    public Desconto getDesconto() {
        return desconto;
    }

    public Boolean getPagamentoRealizado() {
        return pagamentoRealizado;
    }

    public LocalDate getDataVenda() {
        return dataVenda;
    }

    public List<UUID> getPedidosIds() {
        return new ArrayList<>(pedidosIds);
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }
}
