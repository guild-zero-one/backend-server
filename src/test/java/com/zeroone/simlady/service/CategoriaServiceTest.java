package com.zeroone.simlady.service;

import com.zeroone.simlady.entity.Categoria;
import com.zeroone.simlady.exception.ResourceAlreadyExistsException;
import com.zeroone.simlady.exception.ResourceNotFoundException;
import com.zeroone.simlady.repository.CategoriaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private CategoriaService categoriaService;

    private Categoria categoria;

    @BeforeEach
    void setUp() {
        categoria = new Categoria();
        categoria.setId(UUID.randomUUID());
        categoria.setNome("masculino");
        categoria.setDescricao("Produtos para o público masculino");
    }

    @Test
    void testCriarCategoria_Sucesso() {
        // Arrange
        when(categoriaRepository.findByNomeIgnoreCase(anyString())).thenReturn(Optional.empty());
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(categoria);

        // Act
        Categoria resultado = categoriaService.criar(categoria);

        // Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.getNome()).isEqualTo("masculino");
        verify(categoriaRepository, times(1)).save(any(Categoria.class));
    }

    @Test
    void testCriarCategoria_JaExiste() {
        // Arrange
        when(categoriaRepository.findByNomeIgnoreCase(anyString())).thenReturn(Optional.of(categoria));

        // Act & Assert
        assertThatThrownBy(() -> categoriaService.criar(categoria))
                .isInstanceOf(ResourceAlreadyExistsException.class)
                .hasMessageContaining("Categoria com este nome já existe");

        verify(categoriaRepository, never()).save(any(Categoria.class));
    }

    @Test
    void testBuscarPorId_Sucesso() {
        // Arrange
        UUID id = categoria.getId();
        when(categoriaRepository.findById(id)).thenReturn(Optional.of(categoria));

        // Act
        Categoria resultado = categoriaService.buscarPorId(id);

        // Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(id);
        verify(categoriaRepository, times(1)).findById(id);
    }

    @Test
    void testBuscarPorId_NaoEncontrada() {
        // Arrange
        UUID id = UUID.randomUUID();
        when(categoriaRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> categoriaService.buscarPorId(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Categoria não encontrada");
    }

    @Test
    void testListarPaginado() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<Categoria> page = new PageImpl<>(List.of(categoria), pageable, 1);
        when(categoriaRepository.findAll(pageable)).thenReturn(page);

        // Act
        Page<Categoria> resultado = categoriaService.listar(pageable);

        // Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.getContent()).hasSize(1);
        assertThat(resultado.getTotalElements()).isEqualTo(1);
        verify(categoriaRepository, times(1)).findAll(pageable);
    }

    @Test
    void testBuscarPorNome_CaseInsensitive() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<Categoria> page = new PageImpl<>(List.of(categoria), pageable, 1);
        when(categoriaRepository.findByNomeContainingIgnoreCase("MASCULINO", pageable)).thenReturn(page);

        // Act
        Page<Categoria> resultado = categoriaService.buscarPorNome("MASCULINO", pageable);

        // Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.getContent()).hasSize(1);
        verify(categoriaRepository, times(1)).findByNomeContainingIgnoreCase("MASCULINO", pageable);
    }

    @Test
    void testDeletarCategoria_Sucesso() {
        // Arrange
        UUID id = categoria.getId();
        when(categoriaRepository.existsById(id)).thenReturn(true);

        // Act
        categoriaService.deletar(id);

        // Assert
        verify(categoriaRepository, times(1)).deleteById(id);
    }

    @Test
    void testDeletarCategoria_NaoEncontrada() {
        // Arrange
        UUID id = UUID.randomUUID();
        when(categoriaRepository.existsById(id)).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> categoriaService.deletar(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Categoria não encontrada");

        verify(categoriaRepository, never()).deleteById(id);
    }

    @Test
    void testAtualizarCategoria_Sucesso() {
        // Arrange
        UUID id = categoria.getId();
        Categoria categoriaAtualizada = new Categoria();
        categoriaAtualizada.setNome("feminino");
        categoriaAtualizada.setDescricao("Produtos para o público feminino");

        when(categoriaRepository.findById(id)).thenReturn(Optional.of(categoria));
        when(categoriaRepository.findByNomeIgnoreCase("feminino")).thenReturn(Optional.empty());
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(categoria);

        // Act
        Categoria resultado = categoriaService.atualizar(id, categoriaAtualizada);

        // Assert
        assertThat(resultado).isNotNull();
        verify(categoriaRepository, times(1)).save(any(Categoria.class));
    }
}

