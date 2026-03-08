package com.zeroone.simlady.service;

import com.zeroone.simlady.entity.Usuario;
import com.zeroone.simlady.entity.enums.Permissao;
import com.zeroone.simlady.exception.BadRequestException;
import com.zeroone.simlady.exception.ResourceAlreadyExistsException;
import com.zeroone.simlady.exception.ResourceNotFoundException;
import com.zeroone.simlady.repository.UsuarioRepository;
import com.zeroone.simlady.config.security.GerenciadorTokenJwt;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final PasswordEncoder passwordEncoder;
    private final UsuarioRepository usuarioRepository;
    private final GerenciadorTokenJwt gerenciadorTokenJwt;
    private final AuthenticationManager authenticationManager;

    public Usuario cadastrar(Usuario usuario) {

        validarEmail(usuario.getEmail());

        String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(senhaCriptografada);

        return usuarioRepository.save(usuario);
    }

    public String autenticar(Usuario usuario, HttpServletResponse response) {

        final UsernamePasswordAuthenticationToken credentials = new UsernamePasswordAuthenticationToken(
                usuario.getEmail(), usuario.getSenha());

        final Authentication authentication = this.authenticationManager.authenticate(credentials);



        usuarioRepository.findByEmail(usuario.getEmail())
                .orElseThrow(
                        () -> new ResponseStatusException(404, "Email do usuário não cadastrado", null)
                );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token =  gerenciadorTokenJwt.generateToken(authentication);


        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(3600);

        response.addCookie(cookie);

        return token;
    }

    public void atualizarPermissao( UUID id ,String permissao) {

        Usuario usuario = buscar(id);

        if(usuario.getPermissao().equals(Permissao.valueOf(permissao))) {
            throw new ResourceAlreadyExistsException("Usuário já possui essa permissão");
        }

        try{
            usuario.setPermissao(Permissao.valueOf(permissao));
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Permissão inválida.");
        }

        usuarioRepository.save(usuario);

    }

    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    public Usuario buscar(UUID id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));
    }

    public Usuario atualizar(UUID id, Usuario usuario) {

        Usuario usuarioAntigo = buscar(id);



        boolean existePorEmail = usuarioRepository.existsByEmailAndIdNot(usuario.getEmail(), id);

        if(existePorEmail) {
            throw new ResourceAlreadyExistsException("Dados inválidos, email já cadastrado");
        }

        usuarioAntigo.setNome(usuario.getNome());
        usuarioAntigo.setSobrenome(usuario.getSobrenome());
        usuarioAntigo.setEmail(usuario.getEmail());
        usuarioAntigo.setSenha(usuario.getSenha());
        usuarioAntigo.setPermissao(usuario.getPermissao());
        usuarioAntigo.setAtivo(usuario.getAtivo());
        usuarioAntigo.setPedidos(usuario.getPedidos());

        usuario.setId(id);

        return usuarioRepository.save(usuario);
    }

    public Usuario buscarAutenticado(HttpServletRequest request) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String email = auth.getName();

        return usuarioRepository
                .findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
    }

    public void desativar(UUID id) {
        Usuario usuario = buscar(id);
        usuario.setAtivo(false);
        usuarioRepository.save(usuario);
    }

    public void deletar(UUID id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cliente não encontrado");
        }

        usuarioRepository.deleteById(id);
    }

    private void validarEmail(String email) {
        if(usuarioRepository.existsByEmail(email)) {
            throw new ResourceAlreadyExistsException("Email já cadastrado!");
        }
    }
}
