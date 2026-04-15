package com.zeroone.simlady.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "produto")
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String nome;

    private String sku;

    private String descricao;


    private String urlImagem;


    private Integer quantidade;
    private Double precoUnitario;
    private Boolean catalogo;

    private Double valorVenda;

    @ManyToOne
    @JoinColumn(name = "fk_fornecedor", nullable = false)
    private Fornecedor fornecedor;

    @ManyToMany(mappedBy = "produtos", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Categoria> categorias = new HashSet<>();

    @CreationTimestamp
    private LocalDateTime criadoEm;

    @UpdateTimestamp
    private LocalDateTime atualizadoEm;
}
