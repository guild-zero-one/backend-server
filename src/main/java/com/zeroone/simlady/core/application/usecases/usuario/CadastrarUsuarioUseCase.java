package com.zeroone.simlady.core.application.usecases.usuario;

import com.zeroone.simlady.core.application.ports.UsuarioRepositoryPort;
import com.zeroone.simlady.core.domain.usuario.Usuario;
import com.zeroone.simlady.infrastructure.exception.ResourceAlreadyExistsException;
import org.springframework.security.crypto.password.PasswordEncoder;

public class CadastrarUsuarioUseCase {
    private final UsuarioRepositoryPort repository;
    private final PasswordEncoder passwordEncoder;

    public CadastrarUsuarioUseCase(UsuarioRepositoryPort repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario executar(Usuario usuario) {
        // Verifica se já existe um usuário com o mesmo email apenas se o email não for null
        if (usuario.getEmail() != null && !usuario.getEmail().trim().isEmpty()) {
            if (repository.buscarPorEmail(usuario.getEmail()).isPresent()) {
                throw new ResourceAlreadyExistsException("Usuário já existe com o email: " + usuario.getEmail());
            }
        }

        // Criptografar a senha antes de salvar
        String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
        usuario.alterarSenha(senhaCriptografada);

        return repository.salvarUsuario(usuario);
    }
}
