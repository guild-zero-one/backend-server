package com.zeroone.simlady.mapper;

import com.zeroone.simlady.dto.contato.ContatoResponseDto;
import com.zeroone.simlady.entity.Usuario;
import com.zeroone.simlady.entity.enums.StatusPedido;
import com.zeroone.simlady.repository.ContatoRepository;
import com.zeroone.simlady.repository.PedidoVendaRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UsuarioMapperHelper {

    private final PedidoVendaRepository pedidoVendaRepository;
    private final ContatoRepository contatoRepository;
    private final ContatoMapper contatoMapper;

    @Named("mapQtdPedidos")
    public Integer mapQtdPedidos(Usuario usuario) {
        return usuario == null ? 0 : pedidoVendaRepository.countPedidoVendasByUsuario_IdAndStatus(usuario.getId(), StatusPedido.PENDENTE);
    }

    @Named("mapContato")
    public ContatoResponseDto mapContato(Usuario usuario) {
        return usuario == null ? null
                : contatoRepository.findContatoByUsuario_Id(usuario.getId())
                .map(contatoMapper::toDto)
                .orElse(null);
    }
}
