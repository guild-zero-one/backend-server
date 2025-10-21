package com.zeroone.simlady.core.domain.produto;

import com.zeroone.simlady.core.domain.produto.produtoVOs.ImagemUrl;
import com.zeroone.simlady.core.domain.produto.produtoVOs.Sku;
import com.zeroone.simlady.core.domain.produto.produtoVOs.ValorVenda;
import com.zeroone.simlady.core.domain.valueObjects.Descricao;
import com.zeroone.simlady.core.domain.valueObjects.PrecoUnitario;
import com.zeroone.simlady.core.domain.valueObjects.Quantidade;

import java.util.UUID;

public class Produto {
    private UUID id;

    private String nome;

    private Sku sku;

    private Descricao descricao;

    private String tag;

    private Quantidade quantidade;

    private PrecoUnitario precoUnitario;

    private Boolean catalogo;

    private ValorVenda valorVenda;
    
    private ImagemUrl imagemUrl;
    
    private UUID idFornecedor;

    public static Produto of(UUID id, String nome, String sku, String descricao, String tag, Integer quantidade, Double precoUnitario, Boolean catalogo, Double valorVenda, String imagemUrl, UUID idFornecedor) {
        return new Produto(
                id,
                nome,
                Sku.of(sku),
                Descricao.of(descricao),
                tag,
                Quantidade.of(quantidade),
                PrecoUnitario.of(precoUnitario),
                catalogo,
                ValorVenda.of(valorVenda),
                (imagemUrl != null && !imagemUrl.isBlank()) ? ImagemUrl.of(imagemUrl) : null,
                idFornecedor
        );
    }

    public static Produto newProduto(String nome, String sku, String descricao, String tag, Integer quantidade, Double precoUnitario, Boolean catalogo, Double valorVenda, String imagemUrl) {
        return Produto.of(UUID.randomUUID(), nome, sku, descricao, tag, quantidade, precoUnitario, catalogo, valorVenda, imagemUrl, null);
    }

    private Produto(UUID id, String nome, Sku sku, Descricao descricao, String tag, Quantidade quantidade, PrecoUnitario precoUnitario, Boolean catalogo, ValorVenda valorVenda, ImagemUrl imagemUrl, UUID idFornecedor) {
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
    }

    public Produto() {
    }

    public UUID getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Sku getSku() {
        return sku;
    }

    public Descricao getDescricao() {
        return descricao;
    }

    public String getTag() {
        return tag;
    }

    public Quantidade getQuantidade() {
        return quantidade;
    }

    public PrecoUnitario getPrecoUnitario() {
        return precoUnitario;
    }

    public Boolean getCatalogo() {
        return catalogo;
    }

    public ValorVenda getValorVenda() {
        return valorVenda;
    }

    public ImagemUrl getImagemUrl() {
        return imagemUrl;
    }
    
    public UUID getIdFornecedor() {
        return idFornecedor;
    }

    public boolean estaDisponivel() {
        return quantidade.getValue() > 0;
    }

    public boolean estaNoCatalogo() {
        return catalogo != null && catalogo;
    }
}
