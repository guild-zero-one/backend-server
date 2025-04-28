package com.zeroone.simlady.dto.pedido;

import com.zeroone.simlady.dto.pedidoItem.PedidoItemRequestDto;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class PedidoVendaRequestDto {
    @NotNull(message = "ID do Usuário não pode ser nulo.")
    @Schema(description = "ID do Usuário", example = "1")
    private Integer idUsuario;

    @NotNull(message = "Um pedido deve obrigatóriamente conter itens.")
    @ArraySchema(
            schema = @Schema(implementation = PedidoItemRequestDto.class),
            arraySchema = @Schema(description = "Itens do pedido")
    )
    private List<PedidoItemRequestDto> itens;
}
