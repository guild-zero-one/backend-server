package com.zeroone.simlady.infrastructure.persistance.entity;

import com.zeroone.simlady.core.domain.usuario.Permissao;
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
@Table(name = "usuario")
public class UsuarioEntity {
    @Id
    private UUID id;
    
    @Column(nullable = false)
    private String nome;
    
    private String sobrenome;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String senha;
    
    private String celular;
    
    @Column(nullable = false)
    private Boolean ativo = true;
    
    @Enumerated(EnumType.STRING)
    private Permissao permissao;
    
    @CreationTimestamp
    @Column(name = "criado_em")
    private LocalDateTime criadoEm;
    
    @UpdateTimestamp
    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;

    public UsuarioEntity(UUID id, String nome, String sobrenome, String email, String senha, 
                        String celular, Boolean ativo, Permissao permissao, 
                        LocalDateTime criadoEm, LocalDateTime atualizadoEm) {
        this.id = id;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.email = email;
        this.senha = senha;
        this.celular = celular;
        this.ativo = ativo;
        this.permissao = permissao;
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
    }
}
