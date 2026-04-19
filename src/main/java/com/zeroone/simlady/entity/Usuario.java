package com.zeroone.simlady.entity;

import com.zeroone.simlady.entity.enums.Permissao;
import com.zeroone.simlady.entity.enums.Provider;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String nome;

    private String sobrenome;

    @Column
    private String email;

    @Column(unique = true, nullable = true)
    private String clerkId;

    @Column(nullable = true)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private Provider provider;

    private String celular;

    @Column(name = "provider_id",  nullable = true)
    private String providerId;

    @Column(name = "url_imagem")
    private String urlImagem;

    @Schema(description = "Indica se o perfil do usuário está completo", example = "true")
    private Boolean perfilCompleto = true;

    @OneToMany
    private List<PedidoVenda> pedidos;

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
}
