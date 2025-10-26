package com.zeroone.simlady.core.adapters.dtos.produto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlterarStatusCatalogoRequestDto {
    
    @NotNull(message = "Status do catálogo é obrigatório")
    private Boolean catalogo;
}
