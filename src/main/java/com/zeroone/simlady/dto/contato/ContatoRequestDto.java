package com.zeroone.simlady.dto.contato;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ContatoRequestDto {
    @Size(min = 11, max = 11, message = "O número de celular deve ter exatamente 11 dígitos")
    @Pattern(regexp = "\\d{11}", message = "O número de celular deve conter apenas números")
    @Schema(description = "Número de celular", example = "12345678910")
    private String celular;
}
