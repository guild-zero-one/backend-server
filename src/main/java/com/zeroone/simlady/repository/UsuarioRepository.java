package com.zeroone.simlady.repository;

import com.zeroone.simlady.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    boolean existsByEmail(String email);

    List<Usuario> findByEmailAndIdNot(String email, Integer id);

    Boolean existsByEmailAndIdNot(String email, Integer id);

    Optional<Usuario> findByEmail(String email);
}
