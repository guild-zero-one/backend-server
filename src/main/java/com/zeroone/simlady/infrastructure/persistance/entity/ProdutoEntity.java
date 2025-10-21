package com.zeroone.simlady.infrastructure.persistance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "produto")
public class ProdutoEntity {
    @Id
    private UUID id;

    private String nome;

    private String sku;

    private String descricao;

    private String tag;

    private Integer quantidade;

    private Double precoUnitario;

    private Boolean catalogo;

    private Double valorVenda;

    private String imagemUrl;
    
    @Column(name = "fornecedor_id")
    private UUID idFornecedor;

    @CreationTimestamp
    private LocalDateTime criadoEm;

    @UpdateTimestamp
    private LocalDateTime atualizadoEm;

    public ProdutoEntity(UUID id, String nome, String sku, String descricao, String tag, Integer quantidade, Double precoUnitario, Boolean catalogo, Double valorVenda, String imagemUrl, UUID idFornecedor, LocalDateTime criadoEm, LocalDateTime atualizadoEm) {
        this.id = id;
        this.nome = nome;
        this.sku = sku;
        this.descricao = descricao;
        this.tag = tag;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.catalogo = catalogo;
        this.valorVenda = valorVenda;
        this.imagemUrl = imagemUrl;
        this.idFornecedor = idFornecedor;
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
    }
}
