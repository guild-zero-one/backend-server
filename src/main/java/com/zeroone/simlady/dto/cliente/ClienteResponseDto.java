package com.zeroone.simlady.dto.cliente;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ClienteResponseDto {

    private Integer id;
    private String nome;
    private String sobrenome;
    private String apelido;
    private String cpf;
    private String email;
    private Boolean ativo;

}
