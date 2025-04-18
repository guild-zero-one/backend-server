package com.zeroone.simlady.dto.pedido;

import com.zeroone.simlady.dto.item.PedidoItemResponseDto;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class PedidoVendaResponseDto {
    private Integer id;
    private String status;
    private Integer idCliente;
    private Integer idVenda;
    private LocalDate criadoEm;
    private LocalDate atualizadoEm;
    private List<PedidoItemResponseDto> itens;
}
