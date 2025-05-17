package com.zeroone.simlady.mapper;
import com.zeroone.simlady.dto.usuario.*;

import com.zeroone.simlady.dto.usuario.*;
import com.zeroone.simlady.entity.Usuario;
import com.zeroone.simlady.entity.enums.Permissao;
import com.zeroone.simlady.exception.BadRequestException;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {PedidoVendaMapper.class, ContatoMapper.class})
public interface UsuarioMapper {
    Usuario toEntity (UsuarioRequestDto dto);

    Usuario toEntity (UsuarioAtualizacaoDto dto);

    UsuarioResponseDto toDto (Usuario entity);

    @Mapping(target = "qtdPedidos", source = "usuario", qualifiedByName = "mapQtdPedidos")
    @Mapping(target = "contato", source = "usuario", qualifiedByName = "mapContato")
    UsuarioClienteDto toClienteDto(Usuario usuario);

    Usuario toEntity(UsuarioLoginDto dto);

    UsuarioTokenDto toTokenDto(Usuario entity);

    default Permissao toPermissao(String permissao) {
        if (permissao == null) {
            throw new BadRequestException("Permissão não pode ser nula.");
        }

        for (Permissao p : Permissao.values()) {
            if (p.name().equalsIgnoreCase(permissao)) {
                return p;
            }
        }

        throw new BadRequestException("Permissão inválida: " + permissao);
    }
}


