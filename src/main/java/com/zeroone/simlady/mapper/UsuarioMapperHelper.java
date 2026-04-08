package com.zeroone.simlady.mapper;

import com.zeroone.simlady.entity.Usuario;
import com.zeroone.simlady.entity.enums.StatusPedido;
import com.zeroone.simlady.repository.PedidoVendaRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UsuarioMapperHelper {

    private final PedidoVendaRepository pedidoVendaRepository;

    @Named("mapQtdPedidos")
    public Integer mapQtdPedidos(Usuario usuario) {
        return usuario == null ? 0 : pedidoVendaRepository.countPedidoVendasByUsuario_IdAndStatus(usuario.getId(), StatusPedido.PENDENTE);
    }
}
