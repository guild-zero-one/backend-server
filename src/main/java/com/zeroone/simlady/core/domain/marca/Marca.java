package com.zeroone.simlady.core.domain.marca;

import com.zeroone.simlady.core.domain.valueObjects.ImagemUrl;
import com.zeroone.simlady.core.domain.valueObjects.Descricao;

import java.time.LocalDateTime;
import java.util.UUID;

public class Marca {
    private UUID id;
    private String nome;
    private Descricao descricao;
    private ImagemUrl imagemUrl;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

    private Marca(UUID id, String nome, Descricao of, ImagemUrl imagemUrl, LocalDateTime criadoEm, LocalDateTime atualizadoEm) {
        this.id = id;
        this.nome = nome;
        this.descricao = of;
        this.imagemUrl = imagemUrl;
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
    }

    public static Marca of(UUID id, String nome, String descricao, String imagemUrl, LocalDateTime criadoEm, LocalDateTime atualizadoEm) {
        return new Marca(
                id,
                nome,
                (descricao != null && !descricao.isBlank()) ? Descricao.of(descricao) : null,
                (imagemUrl != null && !imagemUrl.isBlank()) ? ImagemUrl.of(imagemUrl) : null,
                criadoEm,
                atualizadoEm
        );
    }

    public static Marca newMarca(String nome, String descricao, String imagemUrl) {
        return Marca.of(
                UUID.randomUUID(),
                nome,
                descricao,
                imagemUrl,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    public UUID getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Descricao getDescricao() {
        return descricao;
    }

    public ImagemUrl getImagemUrl() {
        return imagemUrl;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }
}
