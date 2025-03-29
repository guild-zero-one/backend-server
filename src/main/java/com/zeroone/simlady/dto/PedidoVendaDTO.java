package com.zeroone.simlady.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PedidoVendaDTO {

    private Integer id;
    private String status;
    private Integer idVenda = -1;
    private Integer idCliente;
    private LocalDate criadoEm;
    private LocalDate atualizadoEm;
}
