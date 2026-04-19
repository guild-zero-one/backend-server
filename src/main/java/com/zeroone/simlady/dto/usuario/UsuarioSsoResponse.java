package com.zeroone.simlady.dto.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UsuarioSsoResponse {

    @NotBlank
    private String nome;

    @Email
    @NotBlank
    private String email;

    private String urlImagem;

}
