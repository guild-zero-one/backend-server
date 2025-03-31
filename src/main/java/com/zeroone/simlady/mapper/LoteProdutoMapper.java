package com.zeroone.simlady.mapper;

import com.zeroone.simlady.dto.LoteProdutoDTO;
import com.zeroone.simlady.entity.LoteProduto;

public class LoteProdutoMapper {

    public static LoteProdutoDTO toDTO(LoteProduto loteProduto) {
        if (loteProduto == null) {
            return null;
        }

        LoteProdutoDTO dto = new LoteProdutoDTO();
        dto.setId(loteProduto.getId());
        dto.setQtdLote(loteProduto.getQtdLote());
        dto.setValorUnitCompra(loteProduto.getValorUnitCompra());
        dto.setDataValidade(loteProduto.getDataValidade());
        dto.setProdutoId(loteProduto.getProduto().getId());
        dto.setCriadoEm(loteProduto.getCriadoEm());
        dto.setAtualizadoEm(loteProduto.getAtualizadoEm());

        return dto;
    }

    public static LoteProduto toEntity(LoteProdutoDTO dto) {
        if (dto == null) {
            return null;
        }

        LoteProduto loteProduto = new LoteProduto();
        loteProduto.setId(dto.getId());
        loteProduto.setQtdLote(dto.getQtdLote());
        loteProduto.setValorUnitCompra(dto.getValorUnitCompra());
        loteProduto.setDataValidade(dto.getDataValidade());
        loteProduto.setCriadoEm(dto.getCriadoEm());
        loteProduto.setAtualizadoEm(dto.getAtualizadoEm());

        return loteProduto;
    }
}
