package com.zeroone.simlady.dto.pedido;

import com.zeroone.simlady.dto.item.PedidoItemRequestDto;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class PedidoVendaRequestDto {

    private Integer idUsuario;

    @NotNull
    private List<PedidoItemRequestDto> itens;
}
