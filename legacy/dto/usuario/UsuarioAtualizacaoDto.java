package com.zeroone.simlady.dto.usuario;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.br.CPF;

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

    @CPF(message = "CPF inválido.")
    @Schema(description = "CPF do usuário", example = "451.967.950-81")
    private String cpf;
}
