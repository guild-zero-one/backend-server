package com.zeroone.simlady.dto.pedido;

import com.zeroone.simlady.dto.pedidoItem.PedidoItemRequestDto;
import com.zeroone.simlady.dto.pedidoItem.PedidoItemResponseDto;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class PedidoVendaResponseDto {
    @Schema(description = "ID do Pedido", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID id;

    @Schema(description = "Status do pedido", example = "PENDENTE")
    private String status;

    @Schema(description = "ID do Usuário", example = "550e8400-e29b-41d4-a716-446655440001")
    private UUID idUsuario;

    @Schema(description = "ID da Venda", example = "550e8400-e29b-41d4-a716-446655440002")
    private UUID idVenda;

    @Schema(description = "Data de criação do pedido", example = "2023-10-01T10:15:30")
    private LocalDateTime criadoEm;

    @ArraySchema(
            schema = @Schema(implementation = PedidoItemRequestDto.class),
            arraySchema = @Schema(description = "Itens do pedido")
    )
    private List<PedidoItemResponseDto> itens;
}
