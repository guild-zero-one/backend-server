package com.zeroone.simlady.infrastructure.persistance.adapter;

import com.zeroone.simlady.core.application.ports.UsuarioRepositoryPort;
import com.zeroone.simlady.core.domain.usuario.Usuario;
import com.zeroone.simlady.core.domain.usuario.Permissao;
import com.zeroone.simlady.infrastructure.persistance.entity.UsuarioEntity;
import com.zeroone.simlady.infrastructure.persistance.mapper.UsuarioMapper;
import com.zeroone.simlady.infrastructure.persistance.repository.UsuarioRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class UsuarioJpaAdapter implements UsuarioRepositoryPort {

    private final UsuarioRepositoryImpl repository;

    @Override
    public Usuario salvarUsuario(Usuario usuario) {
        UsuarioEntity entity = UsuarioMapper.toEntity(usuario);
        UsuarioEntity saved = repository.save(entity);
        return UsuarioMapper.toDomain(saved);
    }

    @Override
    public Optional<Usuario> buscarPorId(UUID id) {
        return repository.findById(id)
                .map(UsuarioMapper::toDomain);
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        return repository.findByEmail(email)
                .map(UsuarioMapper::toDomain);
    }

    @Override
    public void deletarPorId(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public Usuario atualizarUsuario(Usuario usuario) {
        UsuarioEntity entity = UsuarioMapper.toEntity(usuario);
        UsuarioEntity saved = repository.save(entity);
        return UsuarioMapper.toDomain(saved);
    }

    @Override
    public List<Usuario> listarTodos() {
        return repository.findAll().stream()
                .map(UsuarioMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Usuario> listarPorPermissao(Permissao permissao) {
        return repository.findByPermissao(permissao).stream()
                .map(UsuarioMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Usuario> listarAtivos() {
        return repository.findByAtivoTrue().stream()
                .map(UsuarioMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Usuario> listarInativos() {
        return repository.findByAtivoFalse().stream()
                .map(UsuarioMapper::toDomain)
                .collect(Collectors.toList());
    }
}
