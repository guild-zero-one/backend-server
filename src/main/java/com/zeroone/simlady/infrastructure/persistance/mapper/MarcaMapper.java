package com.zeroone.simlady.infrastructure.persistance.mapper;

import com.zeroone.simlady.core.adapters.dtos.marca.*;
import com.zeroone.simlady.core.domain.marca.Marca;
import com.zeroone.simlady.infrastructure.persistance.entity.MarcaEntity;

public class MarcaMapper {

    public static MarcaEntity toEntity (Marca marca){
        return new MarcaEntity(
                marca.getId(),
                marca.getNome(),
                marca.getDescricao() != null ? marca.getDescricao().getValue() : null,
                marca.getImagemUrl() != null ? marca.getImagemUrl().toString() : null,
                marca.getCriadoEm(),
                marca.getAtualizadoEm()
        );
    }

    public static Marca toRawMarca (MarcaEntity entity){
        return Marca.of (
                entity.getId(),
                entity.getNome(),
                entity.getDescricao(),
                entity.getImagemUrl(),
                entity.getCriadoEm(),
                entity.getAtualizadoEm()
        );
    }

    public static Marca toRawMarca (MarcaRequestDto request){
        return Marca.newMarca (
                request.nome(),
                request.descricao(),
                request.imagemUrl()
        );
    }

    public static MarcaResponseDto toResponseDto (Marca marca){
        return new MarcaResponseDto(
                marca.getId(),
                marca.getNome(),
                marca.getDescricao() != null ? marca.getDescricao().getValue() : null,
                marca.getImagemUrl() != null ? marca.getImagemUrl().toString() : null,
                marca.getCriadoEm(),
                marca.getAtualizadoEm()
        );
    }

}
