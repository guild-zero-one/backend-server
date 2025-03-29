package com.zeroone.simlady.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContatoDTO {
    private Integer id;
    private String celular;
    private LocalDate criadoEm;
    private LocalDate atualizadoEm;
    

}

