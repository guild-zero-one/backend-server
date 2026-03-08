package com.zeroone.simlady.dto.pedido;

import com.zeroone.simlady.dto.pedidoItem.PedidoItemResponseDto;
import lombok.Data;

import java.util.List;

@Data
public class PedidoMensagemDto {
    private Integer id;
    private String nomeUsuario;
    private String celularUsuario;
    private List<PedidoItemResponseDto> itens;
}