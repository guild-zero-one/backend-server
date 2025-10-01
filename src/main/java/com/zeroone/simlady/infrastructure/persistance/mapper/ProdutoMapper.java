package com.zeroone.simlady.infrastructure.persistance.mapper;

import com.zeroone.simlady.core.adapters.dtos.produto.ProdutoRequestDto;
import com.zeroone.simlady.core.adapters.dtos.produto.ProdutoResponseDto;
import com.zeroone.simlady.core.domain.produto.Produto;
import com.zeroone.simlady.infrastructure.persistance.entity.ProdutoEntity;

import java.util.UUID;

public class ProdutoMapper {
    public static ProdutoEntity toEntity(Produto produto){
        return new ProdutoEntity(
                produto.getId().toString(),
                produto.getNome(),
                produto.getSku().getValue(),
                produto.getDescricao().getValue(),
                produto.getTag(),
                produto.getQuantidade().getValue(),
                produto.getPrecoUnitario().getValue(),
                produto.getCatalogo(),
                produto.getValorVenda().getValue()
        );
    }

    public static Produto toRawProduto(ProdutoEntity entity){
        return Produto.of(
                UUID.fromString(entity.getId()),
                entity.getNome(),
                entity.getSku(),
                entity.getDescricao(),
                entity.getTag(),
                entity.getQuantidade(),
                entity.getPrecoUnitario(),
                entity.getCatalogo(),
                entity.getValorVenda()
        );
    }

    public static Produto toRawProduto(ProdutoRequestDto dto){
        return Produto.newProduto(
                dto.nome(),
                dto.sku(),
                dto.descricao(),
                dto.tag(),
                dto.quantidade(),
                dto.precoUnitario(),
                dto.catalogo(),
                dto.valorVenda()
        );
    }

    public static ProdutoResponseDto toResponseDto(Produto produto){
        return new ProdutoResponseDto(
                produto.getId().toString(),
                produto.getNome(),
                produto.getSku().getValue(),
                produto.getDescricao().getValue(),
                produto.getTag(),
                produto.getQuantidade().getValue(),
                produto.getPrecoUnitario().getValue(),
                produto.getCatalogo(),
                produto.getValorVenda().getValue()
        );
    }
}
