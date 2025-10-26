package com.zeroone.simlady.infrastructure.persistance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "venda")
public class VendaEntity {
    @Id
    private UUID id;
    
    @Column(name = "valor_total", precision = 10, scale = 2)
    private BigDecimal valorTotal;
    
    @Column(name = "desconto", precision = 10, scale = 2)
    private BigDecimal desconto;
    
    @Column(name = "pagamento_realizado")
    private Boolean pagamentoRealizado;
    
    @Column(name = "data_venda")
    private LocalDate dataVenda;
    
    @ElementCollection
    @CollectionTable(name = "venda_pedidos", joinColumns = @JoinColumn(name = "venda_id"))
    @Column(name = "pedido_id")
    private List<UUID> pedidosIds = new ArrayList<>();
    
    @CreationTimestamp
    @Column(name = "criado_em")
    private LocalDateTime criadoEm;
    
    @UpdateTimestamp
    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;

    public VendaEntity(UUID id, BigDecimal valorTotal, BigDecimal desconto,
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
}
