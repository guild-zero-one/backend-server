package com.zeroone.simlady.mapper;

import com.zeroone.simlady.dto.FornecedorDTO;
import com.zeroone.simlady.entity.Fornecedor;

public class FornecedorMapper {

    public static FornecedorDTO toDTO(Fornecedor fornecedor) {
        if (fornecedor == null) {
            return null;
        }

        FornecedorDTO dto = new FornecedorDTO();
        dto.setId(fornecedor.getId());
        dto.setNome(fornecedor.getNome());
        dto.setCnpj(fornecedor.getCnpj());
        dto.setCriadoEm(fornecedor.getCriadoEm());
        dto.setAtualizadoEm(fornecedor.getAtualizadoEm());

        return dto;
    }

    public static Fornecedor toEntity(FornecedorDTO dto) {
        if (dto == null) {
            return null;
        }

        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setId(dto.getId());
        fornecedor.setNome(dto.getNome());
        fornecedor.setCnpj(dto.getCnpj());
        fornecedor.setCriadoEm(dto.getCriadoEm());
        fornecedor.setAtualizadoEm(dto.getAtualizadoEm());

        return fornecedor;
    }
}