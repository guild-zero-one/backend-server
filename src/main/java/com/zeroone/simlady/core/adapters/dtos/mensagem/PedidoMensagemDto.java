package com.zeroone.simlady.core.adapters.dtos.mensagem;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record PedidoMensagemDto(
        UUID pedidoId,
        UUID usuarioId,
        String nomeUsuario,
        Set<String> contatosUsuario,
        LocalDateTime dataCriacao,
        String status,
        Double valorTotal
) implements Serializable {}
