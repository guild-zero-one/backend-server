package com.zeroone.simlady.infrastructure.persistance.mapper;

import com.zeroone.simlady.core.adapters.dtos.fornecedor.*;
import com.zeroone.simlady.core.domain.fornecedor.Fornecedor;
import com.zeroone.simlady.infrastructure.persistance.entity.FornecedorEntity;

public class FornecedorMapper {

    public static FornecedorEntity toEntity (Fornecedor fornecedor){
        return new FornecedorEntity(
                fornecedor.getId(),
                fornecedor.getNome(),
                fornecedor.getDescricao().getValue(),
                fornecedor.getCnpj().toString(),
                fornecedor.getImagemUrl().toString(),
                fornecedor.getCriadoEm(),
                fornecedor.getAtualizadoEm()
        );
    }

    public static Fornecedor toRawFornecedor (FornecedorEntity entity){
        return Fornecedor.of (
                entity.getId(),
                entity.getNome(),
                entity.getDescricao(),
                entity.getCnpj(),
                entity.getImagemUrl(),
                entity.getCriadoEm(),
                entity.getAtualizadoEm()
        );
    }

    public static Fornecedor toRawFornecedor (FornecedorRequestDto request){
        return Fornecedor.newFornecedor (
                request.nome(),
                request.descricao(),
                request.cnpj(),
                request.imagemUrl()
        );
    }

    public static FornecedorResponseDto toResponseDto (Fornecedor fornecedor){
        return new FornecedorResponseDto(
                fornecedor.getId(),
                fornecedor.getNome(),
                fornecedor.getDescricao().getValue(),
                fornecedor.getCnpj().toString(),
                fornecedor.getImagemUrl().toString(),
                fornecedor.getCriadoEm(),
                fornecedor.getAtualizadoEm()
        );
    }

}
