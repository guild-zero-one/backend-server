package com.zeroone.simlady.dto.usuario;

import com.zeroone.simlady.dto.contato.ContatoResponseDto;
import com.zeroone.simlady.dto.pedidoItem.PedidoItemRequestDto;
import com.zeroone.simlady.entity.Contato;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UsuarioClienteDto {
    @Schema(description = "ID do Usuário", example = "1")
    private Integer id;

    @Schema(description = "Nome do usuário", example = "André")
    private String nome;

    @Schema(description = "E-mail do usuário", example = "andre@gmail.com")
    private String email;

    @Schema(description = "Usuário ativo", example = "true")
    private Boolean ativo;

    @Schema(description = "Imagem de Perfil", example = "blob.url.com")
    private String urlImagem;

    @Schema(description = "Quantidade de Pedidos", example = "34")
    private Integer qtdPedidos;

    @Schema(description = "Contato do Usuário", implementation = PedidoItemRequestDto.class)
    private ContatoResponseDto contato;
}
