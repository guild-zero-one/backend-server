package com.zeroone.simlady.service_test;

import com.zeroone.simlady.entity.Contato;
import com.zeroone.simlady.entity.Usuario;
import com.zeroone.simlady.exception.ResourceAlreadyExistsException;
import com.zeroone.simlady.exception.ResourceNotFoundException;
import com.zeroone.simlady.repository.ContatoRepository;
import com.zeroone.simlady.repository.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContatoServiceTest {

    @Mock
    private ContatoRepository contatoRepository;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private ContatoService contatoService;

    @Test
    @DisplayName("Deve adicionar um novo contato com sucesso")
    void deveAdicionarContatoComSucesso() {
        Integer clienteId = 1;
        Contato contato = new Contato();
        contato.setCelular("123456789");
        Usuario usuario = mock(Usuario.class);

        when(usuarioService.buscar(clienteId)).thenReturn(usuario);
        when(contatoRepository.existsByCelular(contato.getCelular())).thenReturn(false);

        Contato result = contatoService.adicionar(clienteId, contato);

        assertEquals(contato, result);
        verify(contatoRepository).save(contato);
        verify(usuario).adicionarContato(contato);
    }

    @Test
    @DisplayName("Deve lançar exceção ao adicionar contato com celular já existente")
    void deveLancarExcecaoAoAdicionarContatoComCelularExistente() {
        Integer clienteId = 1;
        Contato contato = new Contato();
        contato.setCelular("123456789");
        when(contatoRepository.existsByCelular(contato.getCelular())).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> contatoService.adicionar(clienteId, contato));
        verify(contatoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve buscar contato por ID")
    void deveBuscarContatoPorId() {
        Integer id = 1;
        Contato contato = new Contato();
        when(contatoRepository.findById(id)).thenReturn(Optional.of(contato));

        Contato result = contatoService.buscar(id);

        assertEquals(contato, result);
        verify(contatoRepository).findById(id);
    }

    @Test
    @DisplayName("Deve lançar exceção quando contato não for encontrado por ID")
    void deveLancarExcecaoQuandoContatoNaoEncontradoPorId() {
        Integer id = 1;
        when(contatoRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> contatoService.buscar(id));
    }

    @Test
    @DisplayName("Deve buscar contatos por cliente")
    void deveBuscarContatosPorCliente() {
        Integer clienteId = 1;
        Usuario usuario = new Usuario();
        Set<Contato> contatos = new HashSet<>();
        usuario.setContatos(contatos);

        when(usuarioService.buscar(clienteId)).thenReturn(usuario);

        Set<Contato> result = contatoService.buscarPorCliente(clienteId);

        assertEquals(contatos, result);
        verify(usuarioService).buscar(clienteId);
    }

    @Test
    @DisplayName("Deve atualizar contato com sucesso")
    void deveAtualizarContatoComSucesso() {
        Integer id = 1;
        Contato contato = new Contato();
        contato.setCelular("123456789");
        Usuario usuario = new Usuario();

        when(usuarioService.buscar(id)).thenReturn(usuario);
        when(contatoRepository.existsByCelularAndIdNot(contato.getCelular(), id)).thenReturn(false);

        Contato result = contatoService.atualizar(id, contato);

        assertEquals(contato, result);
        assertEquals(id, contato.getId());
        assertEquals(usuario, contato.getUsuario());
        verify(contatoRepository).save(contato);
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar contato com celular já existente")
    void deveLancarExcecaoAoAtualizarContatoComCelularExistente() {
        Integer id = 1;
        Contato contato = new Contato();
        contato.setCelular("123456789");

        when(contatoRepository.existsByCelularAndIdNot(contato.getCelular(), id)).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> contatoService.atualizar(id, contato));
        verify(contatoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve deletar contato com sucesso")
    void deveDeletarContatoComSucesso() {
        Integer id = 1;
        Contato contato = new Contato();
        Usuario usuario = new Usuario();
        contato.setUsuario(usuario);
        usuario.setId(2);
        usuario.setContatos(new HashSet<>(List.of(contato)));

        when(contatoRepository.findById(id)).thenReturn(Optional.of(contato));
        when(usuarioService.buscar(usuario.getId())).thenReturn(usuario);

        contatoService.deletar(id);

        verify(usuarioRepository).save(usuario);
        verify(contatoRepository).delete(contato);
        assertFalse(usuario.getContatos().contains(contato));
    }

}