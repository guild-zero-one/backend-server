package com.zeroone.simlady.dto.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioSsoRequest {

    @NotBlank
    private String nome;

    @Email
    @NotBlank
    private String email;

    private String sobrenome;

    private String urlImagem;
}