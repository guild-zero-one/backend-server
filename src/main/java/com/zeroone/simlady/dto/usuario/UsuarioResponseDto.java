package com.zeroone.simlady.dto.usuario;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

@Data
public class UsuarioResponseDto {
    @Schema(description = "ID do Usuário", example = "1")
    private Integer id;

    @Schema(description = "Nome do usuário", example = "André")
    private String nome;

    @Schema(description = "Sobrenome do usuário", example = "Silva")
    private String sobrenome;

    @Schema(description = "Apelido do usuário", example = "Andy")
    private String apelido;

    @Schema(description = "CPF", example = "12345678910")
    private String cpf;

    @Schema(description = "E-mail do usuário", example = "andre@gmail.com")
    private String email;

    @Schema(description = "Usuário ativo", example = "true")
    private Boolean ativo;

    @Schema(description = "Imagem de Perfil", example = "blob.url.com")
    private String urlImagem;
}
