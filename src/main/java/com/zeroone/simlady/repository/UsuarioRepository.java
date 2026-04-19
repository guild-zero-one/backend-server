package com.zeroone.simlady.repository;

import com.zeroone.simlady.entity.Usuario;
import com.zeroone.simlady.entity.enums.Provider;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID>{

    boolean existsByEmail(@Email(message = "Email inválido") @NotBlank(message = "Email não pode ser vazio") String email);

    boolean existsByEmailAndIdNot(@Email(message = "Email inválido") @NotBlank(message = "Email não pode ser vazio") String email, UUID id);

    List<Usuario> findByEmailAndIdNot(String email, UUID id);

    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByClerkId(String clerkId);

    Optional<Usuario> findByProviderAndProviderId(Provider provider, String providerId);
}
