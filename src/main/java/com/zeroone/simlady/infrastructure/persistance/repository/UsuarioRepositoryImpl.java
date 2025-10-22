package com.zeroone.simlady.infrastructure.persistance.repository;

import com.zeroone.simlady.core.domain.usuario.Permissao;
import com.zeroone.simlady.infrastructure.persistance.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsuarioRepositoryImpl extends JpaRepository<UsuarioEntity, UUID> {
    Optional<UsuarioEntity> findByEmail(String email);
    List<UsuarioEntity> findByPermissao(Permissao permissao);
    List<UsuarioEntity> findByAtivoTrue();
    List<UsuarioEntity> findByAtivoFalse();
}
