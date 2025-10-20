package com.zeroone.simlady.core.domain.fornecedor;

import com.zeroone.simlady.core.domain.fornecedor.fornecedorVOs.Cnpj;
import com.zeroone.simlady.core.domain.fornecedor.fornecedorVOs.ImagemUrl;
import com.zeroone.simlady.core.domain.valueObjects.Descricao;

import java.time.LocalDateTime;
import java.util.UUID;

public class Fornecedor {
    private UUID id;
    private String nome;
    private Descricao descricao;
    private Cnpj cnpj;
    private ImagemUrl imagemUrl;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

    private Fornecedor(UUID id, String nome, Descricao of, Cnpj cnpj, ImagemUrl imagemUrl, LocalDateTime criadoEm, LocalDateTime atualizadoEm) {
        this.id = id;
        this.nome = nome;
        this.descricao = of;
        this.cnpj = cnpj;
        this.imagemUrl = imagemUrl;
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
    }

    public static Fornecedor of(UUID id, String nome, String descricao, String cnpj, String imagemUrl, LocalDateTime criadoEm, LocalDateTime atualizadoEm) {
        return new Fornecedor(
                id,
                nome,
                Descricao.of(descricao),
                Cnpj.of(cnpj),
                (imagemUrl != null && !imagemUrl.isBlank()) ? ImagemUrl.of(imagemUrl) : null,
                criadoEm,
                atualizadoEm
        );
    }

    public static Fornecedor newFornecedor(String nome, String descricao, String cnpj, String imagemUrl) {
        return Fornecedor.of(
                UUID.randomUUID(),
                nome,
                descricao,
                cnpj,
                imagemUrl,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Descricao getDescricao() {
        return descricao;
    }

    public void setDescricao(Descricao descricao) {
        this.descricao = descricao;
    }

    public Cnpj getCnpj() {
        return cnpj;
    }

    public void setCnpj(Cnpj cnpj) {
        this.cnpj = cnpj;
    }

    public ImagemUrl getImagemUrl() {
        return imagemUrl;
    }

    public void setImagemUrl(ImagemUrl imagemUrl) {
        this.imagemUrl = imagemUrl;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }

    public void setAtualizadoEm(LocalDateTime atualizadoEm) {
        this.atualizadoEm = atualizadoEm;
    }
}
