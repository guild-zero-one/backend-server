package com.zeroone.simlady.dto.fornecedor;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.br.CNPJ;

@Data
public class FornecedorRequestDto {
    @NotBlank(message = "Nome não pode ser vazio.")
    @Schema(description = "Nome do fornecedor/marca", example = "Boticário")
    private String nome;



    @NotBlank(message = "Descrição não deve ser vazio")
    private String descricao;
    private String imagemUrl;

    @Schema(description = "CNPJ", example = "10.164.222/0001-01")
    @CNPJ(message = "CNPJ inválido.")
    private String cnpj;
}
