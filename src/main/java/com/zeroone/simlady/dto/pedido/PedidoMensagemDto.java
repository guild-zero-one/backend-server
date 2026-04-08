package com.zeroone.simlady.dto.pedido;

import com.zeroone.simlady.dto.pedidoItem.PedidoItemResponseDto;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class PedidoMensagemDto {
    private UUID id;
    private String nomeUsuario;
    private String celularUsuario;
    private List<PedidoItemResponseDto> itens;
}