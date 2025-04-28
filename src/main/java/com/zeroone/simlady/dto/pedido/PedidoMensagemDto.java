package com.zeroone.simlady.dto.pedido;

import com.zeroone.simlady.dto.contato.ContatoResponseDto;
import com.zeroone.simlady.dto.pedidoItem.PedidoItemResponseDto;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class PedidoMensagemDto {
    private Integer id;
    private String nomeUsuario;
    private Set<String> contatosUsuario;
    private List<PedidoItemResponseDto> itens;
}