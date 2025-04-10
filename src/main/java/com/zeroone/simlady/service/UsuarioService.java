package com.zeroone.simlady.service;

import com.zeroone.simlady.entity.Cliente;
import com.zeroone.simlady.entity.Usuario;
import com.zeroone.simlady.entity.enums.Permissao;
import com.zeroone.simlady.exception.BadRequestException;
import com.zeroone.simlady.exception.ResourceAlreadyExistsException;
import com.zeroone.simlady.exception.ResourceNotFoundException;
import com.zeroone.simlady.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public Usuario cadastrar(Usuario usuario) {

        validarEmail(usuario.getEmail());
        return usuarioRepository.save(usuario);

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
