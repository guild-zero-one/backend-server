package com.zeroone.simlady.dto.usuario;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UsuarioResponseDto {

    private Integer id;
    private String nome;
    private String sobrenome;
    private String apelido;
    private String cpf;
    private String email;
    private Boolean ativo;
}
