package com.zeroone.simlady.service;

import com.zeroone.simlady.entity.Usuario;
import com.zeroone.simlady.entity.enums.Permissao;
import com.zeroone.simlady.exception.BadRequestException;
import com.zeroone.simlady.exception.ResourceAlreadyExistsException;
import com.zeroone.simlady.exception.ResourceNotFoundException;
import com.zeroone.simlady.repository.UsuarioRepository;
import com.zeroone.simlady.config.security.GerenciadorTokenJwt;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final PasswordEncoder passwordEncoder;
    private final UsuarioRepository usuarioRepository;
    private final GerenciadorTokenJwt gerenciadorTokenJwt;
    private final AuthenticationManager authenticationManager;


    public Usuario cadastrar(Usuario usuario) {

        validarCpf(usuario.getCpf());
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

        return gerenciadorTokenJwt.generateToken(authentication);
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

    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    public Usuario buscar(Integer id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));
    }

    public Usuario atualizar(Integer id, Usuario usuario) {

        buscar(id);

        boolean existePorCpf = usuarioRepository.existsByCpfAndIdNot(usuario.getCpf(), id);
        boolean existePorEmail = usuarioRepository.existsByEmailAndIdNot(usuario.getEmail(), id);

        if(existePorCpf && existePorEmail) {
            throw new ResourceAlreadyExistsException("Dados inválidos, email e/ou cpf já cadastrados");
        }

        usuario.setId(id);

        return usuarioRepository.save(usuario);
    }

    public void desativar(Integer id) {
        Usuario usuario = buscar(id);
        usuario.setAtivo(false);
        usuarioRepository.save(usuario);
    }

    public void deletar(Integer id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cliente não encontrado");
        }

        usuarioRepository.deleteById(id);
    }

    private void validarCpf(String cpf) {
        if(usuarioRepository.existsByCpf(cpf)) {
            throw new ResourceAlreadyExistsException("CPF já cadastrado!");
        }
    }

    private void validarEmail(String email) {
        if(usuarioRepository.existsByEmail(email)) {
            throw new ResourceAlreadyExistsException("Email já cadastrado!");
        }
    }
}

