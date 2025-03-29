package com.zeroone.simlady.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProdutoDTO {

    private Integer id;
    private String nome;
    private String nomeFantasia;
    private Integer fornecedorId;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
}