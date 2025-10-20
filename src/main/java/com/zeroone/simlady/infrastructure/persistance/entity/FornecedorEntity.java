package com.zeroone.simlady.infrastructure.persistance.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
public class FornecedorEntity {
    @Id
    private UUID id;
    private String nome;
    private String descricao;
    private String cnpj;
    private String imagemUrl;
    @CreationTimestamp
    private LocalDateTime criadoEm;
    @UpdateTimestamp
    private LocalDateTime atualizadoEm;

    public FornecedorEntity(UUID id, String nome, String descricao, String cnpj, String imagemUrl, LocalDateTime criadoEm, LocalDateTime atualizadoEm) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.cnpj = cnpj;
        this.imagemUrl = imagemUrl;
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
    }
}
