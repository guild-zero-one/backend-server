package com.zeroone.simlady.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;


import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nome;

    private String sku;

    private String desc;

    private String tag;

    private Integer quantidade;

    private Double precoUnitario;

    private Boolean catalogo;

    private Double valorVenda;

    @ManyToOne
    @JoinColumn(name = "fk_fornecedor", nullable = false)
    private Fornecedor fornecedor;

    @CreationTimestamp
    private LocalDateTime criadoEm;

    @UpdateTimestamp
    private LocalDateTime atualizadoEm;
}
