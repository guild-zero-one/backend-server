package com.zeroone.simlady.core.application.usecases.marca;

import com.zeroone.simlady.core.application.ports.MarcaRepositoryPort;
import com.zeroone.simlady.core.domain.marca.Marca;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CriarMarcaUseCaseTest {

    @Mock
    private MarcaRepositoryPort repository;

    @InjectMocks
    private CriarMarcaUseCase criarMarcaUseCase;

    @Test
    @DisplayName("Deve criar marca com sucesso")
    void deveCriarMarcaComSucesso() {
        // Given
        UUID id = UUID.randomUUID();
        Marca marca = Marca.of(id, "Marca Teste", "Descrição da marca", null, null, null);

        Marca marcaSalva = Marca.of(id, "Marca Teste", "Descrição da marca", null, null, null);

        when(repository.salvarMarca(any(Marca.class))).thenReturn(marcaSalva);

        // When
        Marca resultado = criarMarcaUseCase.executar(marca);

        // Then
        assertNotNull(resultado);
        assertEquals("Marca Teste", resultado.getNome());
        assertEquals("Descrição da marca", resultado.getDescricao().getValue());
        verify(repository).salvarMarca(marca);
    }

    @Test
    @DisplayName("Deve criar marca com nome apenas")
    void deveCriarMarcaComNomeApenas() {
        // Given
        Marca marca = Marca.newMarca("Marca Simples", null, null);

        when(repository.salvarMarca(any(Marca.class))).thenReturn(marca);

        // When
        Marca resultado = criarMarcaUseCase.executar(marca);

        // Then
        assertNotNull(resultado);
        assertEquals("Marca Simples", resultado.getNome());
        verify(repository).salvarMarca(marca);
    }

    @Test
    @DisplayName("Deve criar marca com todos os campos")
    void deveCriarMarcaComTodosOsCampos() {
        // Given
        Marca marca = Marca.newMarca("Marca Completa", "Descrição completa da marca", "http://logo.com/marca.jpg");

        when(repository.salvarMarca(any(Marca.class))).thenReturn(marca);

        // When
        Marca resultado = criarMarcaUseCase.executar(marca);

        // Then
        assertNotNull(resultado);
        assertEquals("Marca Completa", resultado.getNome());
        assertEquals("Descrição completa da marca", resultado.getDescricao().getValue());
        assertEquals("http://logo.com/marca.jpg", resultado.getImagemUrl().getValue());
        verify(repository).salvarMarca(marca);
    }
}
