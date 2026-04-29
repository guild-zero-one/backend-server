package com.zeroone.simlady.entity;

import com.zeroone.simlady.entity.enums.StatusPedido;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "pedido_venda")
public class PedidoVenda{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private StatusPedido status = StatusPedido.PENDENTE;

    @ManyToOne
    @JoinColumn(name = "fk_venda")
    private Venda venda;

    @ManyToOne
    @JoinColumn(name = "fk_usuario", nullable = false)
    private Usuario usuario;

    @OneToMany(mappedBy = "pedidoVenda", cascade = CascadeType.ALL)
    private Set<PedidoItem> itens;

    @CreationTimestamp
    private LocalDateTime criadoEm;

    @UpdateTimestamp
    private LocalDateTime atualizadoEm;
}
