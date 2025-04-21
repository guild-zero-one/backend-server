package com.zeroone.simlady.dto.pedido;

import com.zeroone.simlady.dto.item.PedidoItemResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
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

    @Schema(description = "Itens do pedido", implementation = PedidoItemResponseDto.class)
    private List<PedidoItemResponseDto> itens;
}
