package com.zeroone.simlady.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;

@Entity
public class LoteItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer qtdLoteCompra;

    private Double valorUnitarioCompra;

    private LocalDate dataValidade;

    @ManyToOne
    @JoinColumn(name = "produto_id")
    private Produto produto;

    @ManyToOne
    @JoinColumn(name = "lote_id")
    private Lote lote;

    @CreationTimestamp
    private LocalDate criadoEm;

    @UpdateTimestamp
    private LocalDate atualizadoEm;
}
