package com.zeroone.simlady.infrastructure.adapters;

import com.zeroone.simlady.core.application.ports.AutenticacaoPort;
import com.zeroone.simlady.core.application.ports.UsuarioRepositoryPort;
import com.zeroone.simlady.core.domain.usuario.Usuario;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class AutenticacaoAdapter implements AutenticacaoPort {

    private final UsuarioRepositoryPort usuarioRepositoryPort;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails carregarUsuarioPorUsername(String username) {
        log.debug("Carregando usuário por username: {}", username);
        
        Optional<Usuario> usuarioOpt = usuarioRepositoryPort.buscarPorEmail(username);
        
        if (usuarioOpt.isEmpty()) {
            log.warn("Usuário não encontrado: {}", username);
            throw new UsernameNotFoundException("Usuário não encontrado: " + username);
        }
        
        Usuario usuario = usuarioOpt.get();
        
        return User.builder()
                .username(usuario.getEmail())
                .password(usuario.getSenha())
                .authorities(Collections.singletonList(new SimpleGrantedAuthority(usuario.getPermissao().name())))
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(!usuario.isAtivo())
                .build();
    }

    @Override
    public Authentication autenticar(String username, String password) {
        log.debug("Autenticando usuário: {}", username);
        
        UserDetails userDetails = carregarUsuarioPorUsername(username);
        
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            log.warn("Senha inválida para usuário: {}", username);
            throw new BadCredentialsException("Usuário ou senha inválidos");
        }
        
        if (!userDetails.isEnabled()) {
            log.warn("Usuário desabilitado: {}", username);
            throw new BadCredentialsException("Usuário desabilitado");
        }
        
        log.debug("Usuário autenticado com sucesso: {}", username);
        
        return new UsernamePasswordAuthenticationToken(
                userDetails, 
                null, 
                userDetails.getAuthorities()
        );
    }
}
