package com.zeroone.simlady.core.application.usecases.marca;

import com.zeroone.simlady.core.application.ports.MarcaRepositoryPort;
import com.zeroone.simlady.core.domain.marca.Marca;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListarMarcaUseCaseTest {

    @Mock
    private MarcaRepositoryPort repository;

    @InjectMocks
    private ListarMarcaUseCase listarMarcaUseCase;
    
    @BeforeEach
    void setUp() {
        reset(repository);
    }

    @Test
    @DisplayName("Deve listar marcas com sucesso")
    void deveListarMarcasComSucesso() {
        // Given
        int pagina = 0;
        int tamanho = 10;
        
        Marca marca1 = Marca.of(UUID.randomUUID(), "Marca 1", "Descrição 1", null, null, null);
        Marca marca2 = Marca.of(UUID.randomUUID(), "Marca 2", "Descrição 2", null, null, null);

        List<Marca> marcas = List.of(marca1, marca2);
        Page<Marca> page = new PageImpl<>(marcas, PageRequest.of(pagina, tamanho), 2);

        when(repository.listarTodos(eq(pagina), eq(tamanho))).thenReturn(page);

        // When
        Page<Marca> resultado = listarMarcaUseCase.executar(pagina, tamanho);

        // Then
        assertNotNull(resultado);
        assertEquals(2, resultado.getTotalElements());
        assertEquals(2, resultado.getContent().size());
        assertEquals("Marca 1", resultado.getContent().get(0).getNome());
        assertEquals("Marca 2", resultado.getContent().get(1).getNome());
        verify(repository).listarTodos(pagina, tamanho);
    }

    @Test
    @DisplayName("Deve retornar página vazia quando não há marcas")
    void deveRetornarPaginaVaziaQuandoNaoHaMarcas() {
        // Given
        int pagina = 0;
        int tamanho = 10;
        Page<Marca> pageVazia = new PageImpl<>(List.of(), PageRequest.of(pagina, tamanho), 0);

        when(repository.listarTodos(pagina, tamanho)).thenReturn(pageVazia);

        // When
        Page<Marca> resultado = listarMarcaUseCase.executar(pagina, tamanho);

        // Then
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        assertEquals(0, resultado.getTotalElements());
        verify(repository).listarTodos(pagina, tamanho);
    }

    @Test
    @DisplayName("Deve listar marcas com paginação")
    void deveListarMarcasComPaginacao() {
        // Given
        int pagina = 1;
        int tamanho = 5;
        
        Marca marca = Marca.of(UUID.randomUUID(), "Marca Teste", "Descrição da marca", null, null, null);

        List<Marca> marcas = List.of(marca);
        Page<Marca> page = new PageImpl<>(marcas, PageRequest.of(pagina, tamanho), 1);

        when(repository.listarTodos(eq(pagina), eq(tamanho))).thenReturn(page);

        // When
        Page<Marca> resultado = listarMarcaUseCase.executar(pagina, tamanho);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.getContent().size());
        assertEquals(pagina, resultado.getNumber());
        assertEquals(tamanho, resultado.getSize());
        verify(repository).listarTodos(pagina, tamanho);
    }
}
