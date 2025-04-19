package com.zeroone.simlady.dto.contato;

import lombok.*;

@Data
public class ContatoResponseDto {

    private Integer id;
    private String celular;
    private Integer fkUsuario;

}

