package com.zeroone.simlady.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "lote_item")
public class LoteItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer qtdLoteCompra;

    private Double valorUnitarioCompra;

    private LocalDate dataValidade;

    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @ManyToOne
    @JoinColumn(name = "lote_id", nullable = false)
    private Lote lote;

    @CreationTimestamp
    private LocalDate criadoEm;

    @UpdateTimestamp
    private LocalDate atualizadoEm;
}
