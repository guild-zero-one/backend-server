package com.zeroone.simlady.service_test;

import com.zeroone.simlady.config.security.GerenciadorTokenJwt;
import com.zeroone.simlady.entity.Usuario;
import com.zeroone.simlady.exception.BadRequestException;
import com.zeroone.simlady.exception.ResourceAlreadyExistsException;
import com.zeroone.simlady.exception.ResourceNotFoundException;
import com.zeroone.simlady.repository.UsuarioRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private GerenciadorTokenJwt gerenciadorTokenJwt;
    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    @DisplayName("Deve cadastrar usuário com sucesso")
    void deveCadastrarUsuarioComSucesso() {
        Usuario usuario = new Usuario();
        usuario.setCpf("123");
        usuario.setEmail("email@test.com");
        usuario.setSenha("senha");

        when(usuarioRepository.existsByCpf("123")).thenReturn(false);
        when(usuarioRepository.existsByEmail("email@test.com")).thenReturn(false);
        when(passwordEncoder.encode("senha")).thenReturn("senhaCriptografada");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario result = usuarioService.cadastrar(usuario);

        assertEquals(usuario, result);
        assertEquals("senhaCriptografada", usuario.getSenha());
        verify(usuarioRepository).save(usuario);
    }

    @Test
    @DisplayName("Deve lançar exceção ao cadastrar usuário com CPF já cadastrado")
    void deveLancarExcecaoAoCadastrarUsuarioComCpfExistente() {
        Usuario usuario = new Usuario();
        usuario.setCpf("123");
        usuario.setEmail("email@test.com");

        when(usuarioRepository.existsByCpf("123")).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> usuarioService.cadastrar(usuario));
    }

    @Test
    @DisplayName("Deve lançar exceção ao cadastrar usuário com email já cadastrado")
    void deveLancarExcecaoAoCadastrarUsuarioComEmailExistente() {
        Usuario usuario = new Usuario();
        usuario.setCpf("123");
        usuario.setEmail("email@test.com");

        when(usuarioRepository.existsByCpf("123")).thenReturn(false);
        when(usuarioRepository.existsByEmail("email@test.com")).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> usuarioService.cadastrar(usuario));
    }

    @Test
    @DisplayName("Deve autenticar usuário com sucesso")
    void deveAutenticarUsuarioComSucesso() {
        Usuario usuario = new Usuario();
        usuario.setEmail("email@test.com");
        usuario.setSenha("senha");

        HttpServletResponse response = mock(HttpServletResponse.class);
        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(usuarioRepository.findByEmail("email@test.com")).thenReturn(Optional.of(usuario));
        when(gerenciadorTokenJwt.generateToken(authentication)).thenReturn("token");

        String token = usuarioService.autenticar(usuario, response);

        assertEquals("token", token);
        verify(response).addCookie(any());
    }

    @Test
    @DisplayName("Deve lançar exceção ao autenticar usuário com email não cadastrado")
    void deveLancarExcecaoAoAutenticarUsuarioComEmailNaoCadastrado() {
        Usuario usuario = new Usuario();
        usuario.setEmail("email@test.com");
        usuario.setSenha("senha");

        HttpServletResponse response = mock(HttpServletResponse.class);
        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(usuarioRepository.findByEmail("email@test.com")).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> usuarioService.autenticar(usuario, response));
    }

    @Test
    @DisplayName("Deve listar todos os usuários")
    void deveListarTodosUsuarios() {
        Usuario usuario = new Usuario();
        when(usuarioRepository.findAll()).thenReturn(List.of(usuario));

        List<Usuario> result = usuarioService.listar();

        assertEquals(1, result.size());
        verify(usuarioRepository).findAll();
    }

    @Test
    @DisplayName("Deve buscar usuário por ID com sucesso")
    void deveBuscarUsuarioPorIdComSucesso() {
        Usuario usuario = new Usuario();
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));

        Usuario result = usuarioService.buscar(1);

        assertEquals(usuario, result);
        verify(usuarioRepository).findById(1);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar usuário inexistente")
    void deveLancarExcecaoAoBuscarUsuarioInexistente() {
        when(usuarioRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> usuarioService.buscar(1));
    }

    @Test
    @DisplayName("Deve atualizar usuário com sucesso")
    void deveAtualizarUsuarioComSucesso() {
        Usuario usuario = new Usuario();
        usuario.setCpf("123");
        usuario.setEmail("email@test.com");

        when(usuarioRepository.findById(1)).thenReturn(Optional.of(new Usuario()));
        when(usuarioRepository.existsByCpfAndIdNot("123", 1)).thenReturn(false);
        when(usuarioRepository.existsByEmailAndIdNot("email@test.com", 1)).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario result = usuarioService.atualizar(1, usuario);

        assertEquals(usuario, result);
        assertEquals(1, usuario.getId());
        verify(usuarioRepository).save(usuario);
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar usuário com CPF e email já cadastrados")
    void deveLancarExcecaoAoAtualizarUsuarioComCpfEEmailExistentes() {
        Usuario usuario = new Usuario();
        usuario.setCpf("123");
        usuario.setEmail("email@test.com");

        when(usuarioRepository.findById(1)).thenReturn(Optional.of(new Usuario()));
        when(usuarioRepository.existsByCpfAndIdNot("123", 1)).thenReturn(true);
        when(usuarioRepository.existsByEmailAndIdNot("email@test.com", 1)).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> usuarioService.atualizar(1, usuario));
    }

    @Test
    @DisplayName("Deve buscar usuário autenticado com sucesso")
    void deveBuscarUsuarioAutenticadoComSucesso() {
        Usuario usuario = new Usuario();
        usuario.setEmail("email@test.com");

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("email@test.com");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(usuarioRepository.findByEmail("email@test.com")).thenReturn(Optional.of(usuario));

        HttpServletRequest request = mock(HttpServletRequest.class);

        Usuario result = usuarioService.buscarAutenticado(request);

        assertEquals(usuario, result);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar usuário autenticado inexistente")
    void deveLancarExcecaoAoBuscarUsuarioAutenticadoInexistente() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("email@test.com");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(usuarioRepository.findByEmail("email@test.com")).thenReturn(Optional.empty());

        HttpServletRequest request = mock(HttpServletRequest.class);

        assertThrows(ResourceNotFoundException.class, () -> usuarioService.buscarAutenticado(request));
    }

    @Test
    @DisplayName("Deve desativar usuário com sucesso")
    void deveDesativarUsuarioComSucesso() {
        Usuario usuario = new Usuario();
        usuario.setAtivo(true);

        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        usuarioService.desativar(1);

        assertFalse(usuario.getAtivo());
        verify(usuarioRepository).save(usuario);
    }

    @Test
    @DisplayName("Deve deletar usuário com sucesso")
    void deveDeletarUsuarioComSucesso() {
        when(usuarioRepository.existsById(1)).thenReturn(true);

        usuarioService.deletar(1);

        verify(usuarioRepository).deleteById(1);
    }

    @Test
    @DisplayName("Deve lançar exceção ao deletar usuário inexistente")
    void deveLancarExcecaoAoDeletarUsuarioInexistente() {
        when(usuarioRepository.existsById(1)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> usuarioService.deletar(1));
        verify(usuarioRepository, never()).deleteById(anyInt());
    }
}