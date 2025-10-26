package com.zeroone.simlady.core.application.ports;

import com.zeroone.simlady.core.domain.usuario.Usuario;
import com.zeroone.simlady.core.domain.usuario.Permissao;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepositoryPort {
    Usuario salvarUsuario(Usuario usuario);
    Optional<Usuario> buscarPorId(UUID id);
    Optional<Usuario> buscarPorEmail(String email);
    void deletarPorId(UUID id);
    Usuario atualizarUsuario(Usuario usuario);
    List<Usuario> listarTodos();
    List<Usuario> listarPorPermissao(Permissao permissao);
    List<Usuario> listarAtivos();
    List<Usuario> listarInativos();
    Page<Usuario> listarTodos(int pagina, int tamanho);
    Page<Usuario> listarPorPermissao(Permissao permissao, int pagina, int tamanho);
}
