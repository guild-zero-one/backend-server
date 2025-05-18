package com.zeroone.simlady.dto.usuario;

import com.zeroone.simlady.entity.enums.Permissao;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.br.CPF;

@Data
public class UsuarioRequestDto {

    @NotBlank(message = "Nome não pode ser vazio.")
    @Size(min = 3, max = 30, message = "Nome deve ter entre 3 e 30 caracteres.")
    @Schema(description = "Nome do usuário", example = "André")
    private String nome;

    @Size(min = 3, max = 30, message = "Sobrenome deve ter entre 3 e 30 caracteres.")
    @Schema(description = "Sobrenome do usuário", example = "Silva")
    private String sobrenome;

    @CPF(message = "CPF inválido.")
    @Schema(description = "CPF", example = "451.967.950-81")
    private String cpf;

    @Email(message = "E-mail inválido.")
    @NotBlank
    @Schema(description = "E-mail do usuário", example = "andre@gmail.com")
    private String email;

    @NotBlank(message = "Senha não pode ser vazio.")
    @Size(min = 8, max = 30, message = "Senha deve ter entre 8 e 30 caracteres.")
    @Schema(description = "Senha do usuário", example = "12345678")
    private String senha;

    @NotNull(message = "Permissão é obrigatória")
    @Schema(description = "Permissão do usuário", example = "COMUM")
    private Permissao permissao;
}