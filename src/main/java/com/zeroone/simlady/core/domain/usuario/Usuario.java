package com.zeroone.simlady.core.domain.usuario;

import java.time.LocalDateTime;
import java.util.UUID;

public class Usuario {
    private UUID id;
    private String nome;
    private String sobrenome;
    private String email;
    private String senha;
    private String celular;
    private Boolean ativo;
    private Permissao permissao;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

    public Usuario(UUID id, String nome, String sobrenome, String email, String senha, 
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

    public static Usuario of(UUID id, String nome, String sobrenome, String email, String senha, 
                           String celular, Boolean ativo, Permissao permissao, 
                           LocalDateTime criadoEm, LocalDateTime atualizadoEm) {
        return new Usuario(id, nome, sobrenome, email, senha, celular, ativo, permissao, criadoEm, atualizadoEm);
    }

    public static Usuario newUsuario(String nome, String sobrenome, String email, String senha, 
                                   String celular, Permissao permissao) {
        return Usuario.of(
                UUID.randomUUID(),
                nome,
                sobrenome,
                email,
                senha,
                celular,
                true,
                permissao,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    public void atualizarDados(String nome, String sobrenome, String email, String celular) {
        if (nome != null && !nome.trim().isEmpty()) {
            this.nome = nome;
        }
        if (sobrenome != null && !sobrenome.trim().isEmpty()) {
            this.sobrenome = sobrenome;
        }
        if (email != null && !email.trim().isEmpty()) {
            this.email = email;
        }
        if (celular != null && !celular.trim().isEmpty()) {
            this.celular = celular;
        }
        this.atualizadoEm = LocalDateTime.now();
    }

    public void alterarSenha(String novaSenha) {
        if (novaSenha == null || novaSenha.trim().isEmpty()) {
            throw new IllegalArgumentException("Senha não pode ser nula ou vazia");
        }
        this.senha = novaSenha;
        this.atualizadoEm = LocalDateTime.now();
    }

    public void ativar() {
        this.ativo = true;
        this.atualizadoEm = LocalDateTime.now();
    }

    public void desativar() {
        this.ativo = false;
        this.atualizadoEm = LocalDateTime.now();
    }

    public boolean isAtivo() {
        return ativo != null && ativo;
    }

    public boolean isAdmin() {
        return permissao == Permissao.ADMIN;
    }

    public boolean isComum() {
        return permissao == Permissao.COMUM;
    }

    public UUID getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

    public String getCelular() {
        return celular;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public Permissao getPermissao() {
        return permissao;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }
}
