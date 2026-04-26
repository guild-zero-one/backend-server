package com.zeroone.simlady.dto.venda;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AtualizarPagamentoVendaDto {
    @NotNull(message = "Pagamento realizado não pode ser nulo.")
    @Schema(description = "Status de pagamento realizado", example = "true")
    private Boolean pagamentoRealizado;
}

