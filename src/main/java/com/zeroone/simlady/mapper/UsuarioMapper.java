package com.zeroone.simlady.mapper;

import com.zeroone.simlady.dto.usuario.*;

import com.zeroone.simlady.dto.usuario.*;
import com.zeroone.simlady.entity.Usuario;
import com.zeroone.simlady.entity.enums.Permissao;
import com.zeroone.simlady.entity.enums.Provider;
import com.zeroone.simlady.exception.BadRequestException;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {UsuarioMapperHelper.class, PedidoVendaMapper.class})
public interface UsuarioMapper {
    Usuario toEntity (UsuarioRequestDto dto);

    Usuario toEntity (UsuarioAtualizacaoDto dto);

    @Mapping(target = "permissao", source = "permissao")
    UsuarioResponseDto toDto (Usuario entity);

    @Mapping(target = "qtdPedidos", source = "usuario", qualifiedByName = "mapQtdPedidos")
    UsuarioClienteDto toClienteDto(Usuario usuario);

    Usuario toEntity(UsuarioLoginDto dto);

    UsuarioTokenDto toTokenDto(Usuario entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "clerkId", source = "clerkId")
    @Mapping(target = "nome", source = "dto.nome")
    @Mapping(target = "email", source = "dto.email")
    @Mapping(target = "urlImagem", source = "dto.urlImagem")
    @Mapping(target = "provider", ignore = true)
    @Mapping(target = "perfilCompleto", ignore = true)
    Usuario toEntity(UsuarioSsoRequest dto, String clerkId);

    UsuarioSsoResponse toSsoResponse(Usuario entity);

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

    default String map(Permissao permissao) {
        return permissao != null ? permissao.name() : Permissao.COMUM.name();
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "provider", source = "provider")
    @Mapping(target = "providerId", source = "providerId")
    @Mapping(target = "nome", source = "dto.nome")
    @Mapping(target = "email", source = "dto.email")
    @Mapping(target = "urlImagem", source = "dto.urlImagem")
    @Mapping(target = "senha", ignore = true)
    @Mapping(target = "sobrenome", ignore = true)
    @Mapping(target = "celular", ignore = true)
    @Mapping(target = "ativo", constant = "true")
    @Mapping(target = "permissao", ignore = true)
    @Mapping(target = "criadoEm", ignore = true)
    @Mapping(target = "atualizadoEm", ignore = true)
    @Mapping(target = "perfilCompleto", source = "perfilCompleto")
    Usuario toEntity(UsuarioSsoRequest dto, String providerId, Provider provider, Boolean perfilCompleto);
}


