package com.zeroone.simlady.dto.usuario;

import com.zeroone.simlady.entity.enums.Permissao;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UsuarioRequestDto {

    @NotBlank
    @Size(min = 3, max = 30)
    private String nome;


    @Email
    @NotNull
    private String email;

    @Size(min = 8)
    private String senha;

    @NotBlank
    private String permissao;


}
