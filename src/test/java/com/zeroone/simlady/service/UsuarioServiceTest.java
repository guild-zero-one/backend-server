package com.zeroone.simlady.service;

import com.zeroone.simlady.config.security.jwt.GerenciadorTokenJwt;
import com.zeroone.simlady.dto.usuario.UsuarioAtualizacaoDto;
import com.zeroone.simlady.entity.Usuario;
import com.zeroone.simlady.entity.enums.Permissao;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
        usuario.setEmail("email@test.com");
        usuario.setSenha("senha");

        when(usuarioRepository.existsByEmail("email@test.com")).thenReturn(false);
        when(passwordEncoder.encode("senha")).thenReturn("senhaCriptografada");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario result = usuarioService.cadastrar(usuario);

        assertEquals(usuario, result);
        assertEquals("senhaCriptografada", usuario.getSenha());
        verify(usuarioRepository).save(usuario);
    }

    @Test
    @DisplayName("Deve lançar exceção ao cadastrar usuário com email já cadastrado")
    void deveLancarExcecaoAoCadastrarUsuarioComEmailExistente() {
        Usuario usuario = new Usuario();
        usuario.setEmail("email@test.com");

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
    @DisplayName("Deve buscar usuários com filtro admin")
    void deveBuscarUsuariosComFiltroAdmin() {
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Usuario> page = new PageImpl<>(List.of(new Usuario()));

        when(usuarioRepository.findByPermissaoAndNomeContainingIgnoreCaseOrPermissaoAndSobrenomeContainingIgnoreCaseOrPermissaoAndEmailContainingIgnoreCase(
                Permissao.ADMIN, "ana",
                Permissao.ADMIN, "ana",
                Permissao.ADMIN, "ana",
                pageable
        )).thenReturn(page);

        Page<Usuario> result = usuarioService.buscarComFiltro("ana", "admin", pageable);

        assertEquals(1, result.getTotalElements());
        verify(usuarioRepository).findByPermissaoAndNomeContainingIgnoreCaseOrPermissaoAndSobrenomeContainingIgnoreCaseOrPermissaoAndEmailContainingIgnoreCase(
                Permissao.ADMIN, "ana",
                Permissao.ADMIN, "ana",
                Permissao.ADMIN, "ana",
                pageable
        );
    }

    @Test
    @DisplayName("Deve buscar usuários sem filtro de tipo quando type for all")
    void deveBuscarUsuariosSemFiltroTipoQuandoAll() {
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Usuario> page = new PageImpl<>(List.of(new Usuario()));

        when(usuarioRepository.findAll(pageable)).thenReturn(page);

        Page<Usuario> result = usuarioService.buscarComFiltro("   ", "all", pageable);

        assertEquals(1, result.getTotalElements());
        verify(usuarioRepository).findAll(pageable);
    }

    @Test
    @DisplayName("Deve lançar exceção para tipo inválido no filtro")
    void deveLancarExcecaoParaTipoInvalidoNoFiltro() {
        PageRequest pageable = PageRequest.of(0, 10);

        assertThrows(BadRequestException.class, () -> usuarioService.buscarComFiltro("ana", "gerente", pageable));
        verify(usuarioRepository, never()).findByPermissao(any(), any());
        verify(usuarioRepository, never()).findAll(any(org.springframework.data.domain.Pageable.class));
    }

    @Test
    @DisplayName("Deve buscar usuário por ID com sucesso")
    void deveBuscarUsuarioPorIdComSucesso() {
        UUID id = UUID.randomUUID();
        Usuario usuario = new Usuario();
        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));

        Usuario result = usuarioService.buscar(id);

        assertEquals(usuario, result);
        verify(usuarioRepository).findById(id);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar usuário inexistente")
    void deveLancarExcecaoAoBuscarUsuarioInexistente() {
        UUID id = UUID.randomUUID();
        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> usuarioService.buscar(id));
    }

    @Test
    @DisplayName("Deve atualizar usuário com sucesso")
    void deveAtualizarUsuarioComSucesso() {
        UUID id = UUID.randomUUID();
        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setId(id);
        usuarioExistente.setNome("Nome Antigo");
        usuarioExistente.setEmail("antigo@test.com");

        UsuarioAtualizacaoDto usuarioAtualizacao = new UsuarioAtualizacaoDto();
        usuarioAtualizacao.setNome("Nome Novo");
        usuarioAtualizacao.setEmail("email@test.com");

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuarioExistente));
        when(usuarioRepository.existsByEmailAndIdNot("email@test.com", id)).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Usuario result = usuarioService.atualizar(id, usuarioAtualizacao);

        assertEquals(id, result.getId());
        assertEquals("Nome Novo", result.getNome());
        assertEquals("email@test.com", result.getEmail());
        verify(usuarioRepository).save(usuarioExistente);
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar usuário com email já cadastrado")
    void deveLancarExcecaoAoAtualizarUsuarioComEmailExistente() {
        UUID id = UUID.randomUUID();
        UsuarioAtualizacaoDto usuario = new UsuarioAtualizacaoDto();
        usuario.setEmail("email@test.com");

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(new Usuario()));
        when(usuarioRepository.existsByEmailAndIdNot("email@test.com", id)).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> usuarioService.atualizar(id, usuario));
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
    @DisplayName("Deve desativar usuário ao fazer toggle quando ativo")
    void deveDesativarUsuarioAoFazerToggleQuandoAtivo() {
        UUID id = UUID.randomUUID();
        Usuario usuario = new Usuario();
        usuario.setAtivo(true);

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        String resultado = usuarioService.toggleStatus(id);

        assertFalse(usuario.getAtivo());
        assertEquals("Usuário desativado com sucesso.", resultado);
        verify(usuarioRepository).save(usuario);
    }

    @Test
    @DisplayName("Deve ativar usuário ao fazer toggle quando inativo")
    void deveAtivarUsuarioAoFazerToggleQuandoInativo() {
        UUID id = UUID.randomUUID();
        Usuario usuario = new Usuario();
        usuario.setAtivo(false);

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        String resultado = usuarioService.toggleStatus(id);

        assertTrue(usuario.getAtivo());
        assertEquals("Usuário ativado com sucesso.", resultado);
        verify(usuarioRepository).save(usuario);
    }

    @Test
    @DisplayName("Deve lançar exceção ao fazer toggle de status em usuário inexistente")
    void deveLancarExcecaoAoFazerToggleStatusEmUsuarioInexistente() {
        UUID id = UUID.randomUUID();
        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> usuarioService.toggleStatus(id));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve deletar usuário com sucesso")
    void deveDeletarUsuarioComSucesso() {
        UUID id = UUID.randomUUID();
        when(usuarioRepository.existsById(id)).thenReturn(true);

        usuarioService.deletar(id);

        verify(usuarioRepository).deleteById(id);
    }

    @Test
    @DisplayName("Deve lançar exceção ao deletar usuário inexistente")
    void deveLancarExcecaoAoDeletarUsuarioInexistente() {
        UUID id = UUID.randomUUID();
        when(usuarioRepository.existsById(id)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> usuarioService.deletar(id));
        verify(usuarioRepository, never()).deleteById(any(UUID.class));
    }
}
