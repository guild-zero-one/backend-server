package com.zeroone.simlady.mapper;

import com.zeroone.simlady.dto.usuario.UsuarioLoginDto;
import com.zeroone.simlady.dto.usuario.UsuarioRequestDto;
import com.zeroone.simlady.dto.usuario.UsuarioResponseDto;
import com.zeroone.simlady.dto.usuario.UsuarioTokenDto;
import com.zeroone.simlady.entity.Usuario;
import com.zeroone.simlady.entity.enums.Permissao;
import com.zeroone.simlady.exception.BadRequestException;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    Usuario toEntity (UsuarioRequestDto dto);

    UsuarioResponseDto toDto (Usuario entity);

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
