package com.zeroone.simlady.service;

import com.zeroone.simlady.dto.usuario.UsuarioTokenDto;
import com.zeroone.simlady.entity.Cliente;
import com.zeroone.simlady.entity.Usuario;
import com.zeroone.simlady.entity.enums.Permissao;
import com.zeroone.simlady.exception.BadRequestException;
import com.zeroone.simlady.exception.ResourceAlreadyExistsException;
import com.zeroone.simlady.exception.ResourceNotFoundException;
import com.zeroone.simlady.repository.UsuarioRepository;
import com.zeroone.simlady.security.GerenciadorTokenJwt;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RequiredArgsConstructor
@Service
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

    public String autenticar(Usuario usuario) {

        final UsernamePasswordAuthenticationToken credentials = new UsernamePasswordAuthenticationToken(
                usuario.getEmail(), usuario.getSenha());

        final Authentication authentication = this.authenticationManager.authenticate(credentials);

        usuarioRepository.findByEmail(usuario.getEmail())
                        .orElseThrow(
                                () -> new ResponseStatusException(404, "Email do usuário não cadastrado", null)
                        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        final String token = gerenciadorTokenJwt.generateToken(authentication);

        return token;
    }


    public Usuario buscar(Integer id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
    }

    public List<Usuario> listar() {

        return usuarioRepository.findAll();

    }

    public void atualizarPermissao( Integer id ,String permissao) {

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

    public Usuario atualizar(Integer id, Usuario usuario) {
        Usuario usuarioExistente = buscar(id);
        Boolean existe = usuarioRepository.existsByEmailAndIdNot(usuario.getEmail(), usuario.getId());

        if (existe) {
            throw new ResourceAlreadyExistsException("Email já cadastrado");
        }
        usuarioExistente.setId(id);
        usuarioExistente.setEmail(usuario.getEmail());
        usuarioExistente.setNome(usuario.getNome());
        return usuarioRepository.save(usuarioExistente);

    }

    public void deletar(Integer id) {
        Usuario usuario = buscar(id);
        usuarioRepository.delete(usuario);
    }

    private void validarEmail(String email) {
        if(usuarioRepository.existsByEmail(email)) {
            throw new ResourceAlreadyExistsException("Email já cadastrado!");
        }
    }


}
