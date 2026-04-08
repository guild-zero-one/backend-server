package com.zeroone.simlady.dto.venda;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
public class VendaRequestDto {
    @Schema(description = "Desconto da venda", example = "30.00")
    @NotNull
    @PositiveOrZero
    private Double desconto;

    @NotNull(message = "Pagamento realizado não pode ser nulo.")
    @Schema(description = "Pagamento realizado", example = "true")
    private Boolean pagamentoRealizado;

    @NotNull(message = "Uma venda tem que estar atrelada a um pedido.")
    @NotEmpty(message = "Lista de pedidos não pode ser vazia.")
    @Schema(description = "Lista de ID de Pedidos de venda")
    private List<UUID> pedidos;

    private LocalDate dataVenda;
}
