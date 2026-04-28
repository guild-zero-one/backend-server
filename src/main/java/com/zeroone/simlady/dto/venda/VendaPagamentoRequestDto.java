package com.zeroone.simlady.dto.venda;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VendaPagamentoRequestDto {
    @NotNull(message = "pagamentoRealizado não pode ser nulo.")
    @Schema(description = "Novo estado do pagamento", example = "true")
    private Boolean pagamentoRealizado;
}
