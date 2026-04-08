package com.zeroone.simlady.dto.usuario;


import com.zeroone.simlady.entity.enums.Permissao;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class UsuarioClienteDto {
    @Schema(description = "ID do Usuário", example = "1")
    private UUID id;

    @Schema(description = "Nome do usuário", example = "André")
    private String nome;

    @Schema(description = "Sobrenome do usuário", example = "Silva")
    private String sobrenome;

    @Schema(description = "E-mail do usuário", example = "andre@gmail.com")
    private String email;

    @Schema(description = "Usuário ativo", example = "true")
    private Boolean ativo;

    @Schema(description = "Imagem de Perfil", example = "blob.url.com")
    private String urlImagem;

    @Schema(description = "Quantidade de Pedidos", example = "34")
    private Integer qtdPedidos;

    @Schema(description = "Número de celular", example = "12345678910")
    private String celular;

    @Schema(description = "Permissão do usuário", example = "COMUM")
    private Permissao permissao;

    @Schema(description = "Data de criação de usuário", example = "12-12-2012")
    private LocalDate criadoEm;
}
