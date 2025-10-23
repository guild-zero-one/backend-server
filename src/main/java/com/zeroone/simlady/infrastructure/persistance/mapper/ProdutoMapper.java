package com.zeroone.simlady.infrastructure.persistance.mapper;

import com.zeroone.simlady.core.adapters.dtos.produto.ProdutoRequestDto;
import com.zeroone.simlady.core.adapters.dtos.produto.ProdutoResponseDto;
import com.zeroone.simlady.core.adapters.dtos.produto.ProdutoUpdateRequestDto;
import com.zeroone.simlady.core.domain.produto.Produto;
import com.zeroone.simlady.infrastructure.persistance.entity.ProdutoEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ProdutoMapper {
    
    public ProdutoEntity toEntity(Produto produto) {
        return new ProdutoEntity(
                produto.getId(),
                produto.getNome(),
                produto.getSku().getValue(),
                produto.getDescricao().getValue(),
                produto.getTag(),
                produto.getQuantidade().getValue(),
                produto.getPrecoUnitario().getValue(),
                produto.getCatalogo(),
                produto.getValorVenda().getValue(),
                produto.getImagemUrl() != null ? produto.getImagemUrl().getValue() : null,
                produto.getIdMarca(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    public Produto toDomain(ProdutoEntity entity) {
        return Produto.of(
                entity.getId(),
                entity.getNome(),
                entity.getSku(),
                entity.getDescricao(),
                entity.getTag(),
                entity.getQuantidade(),
                entity.getPrecoUnitario(),
                entity.getCatalogo(),
                entity.getValorVenda(),
                entity.getImagemUrl(),
                entity.getIdMarca()
        );
    }

    public Produto toDomainFromRequest(ProdutoRequestDto dto) {
        return Produto.newProduto(
                dto.getNome(),
                dto.getSku(),
                dto.getDescricao(),
                dto.getTag(),
                dto.getQuantidade(),
                dto.getPrecoUnitario(),
                dto.getCatalogo(),
                dto.getValorVenda(),
                dto.getImagemUrl()
        );
    }
    
    public Produto toDomainFromUpdateRequest(ProdutoUpdateRequestDto dto, Produto produtoExistente) {
        if (dto == null || produtoExistente == null) {
            return produtoExistente;
        }
        
        return Produto.of(
                produtoExistente.getId(),
                dto.getNome() != null ? dto.getNome() : produtoExistente.getNome(),
                dto.getSku() != null ? dto.getSku() : produtoExistente.getSku().getValue(),
                dto.getDescricao() != null ? dto.getDescricao() : produtoExistente.getDescricao().getValue(),
                dto.getTag() != null ? dto.getTag() : produtoExistente.getTag(),
                dto.getQuantidade() != null ? dto.getQuantidade() : produtoExistente.getQuantidade().getValue(),
                dto.getPrecoUnitario() != null ? dto.getPrecoUnitario() : produtoExistente.getPrecoUnitario().getValue(),
                dto.getCatalogo() != null ? dto.getCatalogo() : produtoExistente.getCatalogo(),
                dto.getValorVenda() != null ? dto.getValorVenda() : produtoExistente.getValorVenda().getValue(),
                dto.getImagemUrl() != null ? dto.getImagemUrl() : produtoExistente.getImagemUrl().getValue(),
                produtoExistente.getIdMarca()
        );
    }

    public ProdutoResponseDto toResponseDto(Produto produto) {
        ProdutoResponseDto dto = new ProdutoResponseDto();
        dto.setId(produto.getId());
        dto.setNome(produto.getNome());
        dto.setSku(produto.getSku().getValue());
        dto.setDescricao(produto.getDescricao().getValue());
        dto.setTag(produto.getTag());
        dto.setQuantidade(produto.getQuantidade().getValue());
        dto.setPrecoUnitario(produto.getPrecoUnitario().getValue());
        dto.setCatalogo(produto.getCatalogo());
        dto.setValorVenda(produto.getValorVenda().getValue());
        dto.setImagemUrl(produto.getImagemUrl().getValue());
        return dto;
    }
    
    public List<Produto> toDomainList(List<ProdutoEntity> entities) {
        return entities.stream()
                .map(this::toDomain)
                .toList();
    }
}
