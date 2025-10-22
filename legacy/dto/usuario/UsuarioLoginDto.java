package com.zeroone.simlady.dto.usuario;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UsuarioLoginDto {
    @Email(message = "E-mail inválido.")
    @NotBlank(message = "E-mail não pode ser vazio.")
    @Schema(description = "E-mail do usuário", example = "andre@gmail.com")
    private String email;

    @NotBlank(message = "Senha não pode ser vazio.")
    @Schema(description = "Senha do usuário", example = "12345678")
    private String senha;
}
