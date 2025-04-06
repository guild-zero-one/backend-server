package com.zeroone.simlady.dto.cliente;

import com.zeroone.simlady.dto.contato.ContatoResponseListDto;
import com.zeroone.simlady.dto.pedido.PedidoVendaResponseDto;
import lombok.*;

import java.util.Set;

@Data
public class ClienteResponseDto {

    private Integer id;
    private String nome;
    private String sobrenome;
    private String apelido;
    private String cpf;
    private String email;
    private Boolean ativo;

    private Set<ContatoResponseListDto> contatos;
    private Set<PedidoVendaResponseDto> pedidos;
}
