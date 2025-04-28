package com.zeroone.simlady.dto.fornecedor;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CNPJ;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FornecedorRequestDto {
    @NotBlank(message = "Nome não deve ser vazio")
    private String nome;
    @CNPJ(message = "CNPJ Invalido!")
    private String cnpj;
    @NotBlank(message = "Descrição não deve ser vazio")
    private String descricao;
    private String imagemUrl;
}
