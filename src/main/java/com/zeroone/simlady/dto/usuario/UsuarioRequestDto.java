package com.zeroone.simlady.dto.usuario;

import com.zeroone.simlady.entity.enums.Permissao;
import com.zeroone.simlady.entity.enums.Provider;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UsuarioRequestDto {

    @NotBlank(message = "Nome não pode ser vazio.")
    @Size(min = 3, max = 30, message = "Nome deve ter entre 3 e 30 caracteres.")
    @Schema(description = "Nome do usuário", example = "André")
    private String nome;

    @Size(min = 3, max = 30, message = "Sobrenome deve ter entre 3 e 30 caracteres.")
    @Schema(description = "Sobrenome do usuário", example = "Silva")
    private String sobrenome;

    @Email(message = "E-mail inválido.")
    @NotBlank
    @Schema(description = "E-mail do usuário", example = "andre@gmail.com")
    private String email;

    @NotBlank(message = "Senha não pode ser vazio.")
    @Size(min = 8, max = 30, message = "Senha deve ter entre 8 e 30 caracteres.")
    @Schema(description = "Senha do usuário", example = "12345678")
    private String senha;

    @Size(min = 11, max = 11, message = "O número de celular deve ter exatamente 11 dígitos")
    @Pattern(regexp = "\\d{11}", message = "O número de celular deve conter apenas números")
    @Schema(description = "Número de celular", example = "12345678910")
    private String celular;

    @NotNull(message = "Permissão é obrigatória")
    @Schema(description = "Permissão do usuário", example = "COMUM")
    private Permissao permissao;
}