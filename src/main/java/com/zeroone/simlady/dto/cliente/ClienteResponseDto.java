package com.zeroone.simlady.dto.cliente;

import com.zeroone.simlady.dto.contato.ContatoResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

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
    private Set<ContatoResponseDto> contatos = new HashSet<>();

}
