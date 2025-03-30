package com.zeroone.simlady.dto.produto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.logging.log4j.message.Message;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoRequestDto {
    @NotBlank(message = "Nome não deve ser vazio")
    @Size(min = 3, max = 30, message = "Nome deve ter entre 3 e 30 caracteres")
    private String nome;
    @NotBlank(message = "Nome Fantasia não deve ser vazio")
    @Size(min = 3, max = 50, message = "Nome Fantasia deve ter entre 3 e 50 caracteres")
    private String nomeFantasia;
    private Integer fornecedorId;
}
