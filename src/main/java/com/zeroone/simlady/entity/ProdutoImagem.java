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
@Table(name = "produto_imagem")
public class ProdutoImagem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String urlImagem;

    private Boolean imagemPrincipal;

    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @CreationTimestamp
    private LocalDate dataCriacao;

    @UpdateTimestamp
    private LocalDate dataAtualizacao;
}
