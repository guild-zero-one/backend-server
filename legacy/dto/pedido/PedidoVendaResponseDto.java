package com.zeroone.simlady.dto.pedido;

import com.zeroone.simlady.dto.pedidoItem.PedidoItemRequestDto;
import com.zeroone.simlady.dto.pedidoItem.PedidoItemResponseDto;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PedidoVendaResponseDto {
    @Schema(description = "ID do Pedido", example = "1")
    private Integer id;

    @Schema(description = "Status do pedido", example = "PENDENTE")
    private String status;

    @Schema(description = "ID do Usuário", example = "1")
    private Integer idUsuario;

    @Schema(description = "ID da Venda", example = "1")
    private Integer idVenda;

    @Schema(description = "Data de criação do pedido", example = "2023-10-01T10:15:30")
    private LocalDateTime criadoEm;

    @ArraySchema(
            schema = @Schema(implementation = PedidoItemRequestDto.class),
            arraySchema = @Schema(description = "Itens do pedido")
    )
    private List<PedidoItemResponseDto> itens;
}
