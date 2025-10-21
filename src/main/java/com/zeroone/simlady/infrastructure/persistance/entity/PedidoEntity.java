package com.zeroone.simlady.infrastructure.persistance.entity;

import com.zeroone.simlady.core.domain.pedido.StatusPedido;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "pedido")
public class PedidoEntity {
    @Id
    private UUID id;
    
    @Enumerated(EnumType.STRING)
    private StatusPedido status;
    
    @Column(name = "venda_id")
    private UUID idVenda;
    
    @Column(name = "usuario_id", nullable = false)
    private UUID idUsuario;
    
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PedidoItemEntity> itens = new ArrayList<>();
    
    @CreationTimestamp
    @Column(name = "criado_em")
    private LocalDateTime criadoEm;
    
    @UpdateTimestamp
    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;

    public PedidoEntity(UUID id, StatusPedido status, UUID idVenda, UUID idUsuario, 
                       List<PedidoItemEntity> itens, LocalDateTime criadoEm, LocalDateTime atualizadoEm) {
        this.id = id;
        this.status = status;
        this.idVenda = idVenda;
        this.idUsuario = idUsuario;
        this.itens = itens != null ? new ArrayList<>(itens) : new ArrayList<>();
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
    }
}
