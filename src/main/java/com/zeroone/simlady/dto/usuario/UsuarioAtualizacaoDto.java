package com.zeroone.simlady.dto.usuario;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UsuarioAtualizacaoDto {

    @Size(min = 3, max = 30, message = "Nome deve ter entre 3 e 30 caracteres.")
    @Schema(description = "Nome do usuário", example = "André")
    private String nome;

    @Size(min = 3, max = 30, message = "Sobrenome deve ter entre 3 e 30 caracteres.")
    @Schema(description = "Sobrenome do usuário", example = "Silva")
    private String sobrenome;

    @Email(message = "E-mail inválido.")
    @Schema(description = "E-mail do usuário", example = "andre@gmail.com")
    private String email;

    @Size(min = 11, max = 11, message = "O número de celular deve ter exatamente 11 dígitos")
    @Pattern(regexp = "\\d{11}", message = "O número de celular deve conter apenas números")
    @Schema(description = "Número de celular", example = "12345678910")
    private String celular;
}
