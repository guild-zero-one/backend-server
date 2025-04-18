package com.zeroone.simlady.dto.usuario;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UsuarioResponseDto {

    private Integer id;
    private String nome;
    private String email;
    private String permissao;
}
