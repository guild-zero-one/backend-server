package com.zeroone.simlady.core.application.usecases.marca;

import com.zeroone.simlady.core.application.ports.MarcaRepositoryPort;
import com.zeroone.simlady.core.domain.marca.Marca;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BuscarMarcaPorIdUseCaseTest {

    @Mock
    private MarcaRepositoryPort repository;

    @InjectMocks
    private BuscarMarcaPorIdUseCase buscarMarcaPorIdUseCase;

    @Test
    @DisplayName("Deve buscar marca por ID com sucesso")
    void deveBuscarMarcaPorIdComSucesso() {
        // Given
        UUID id = UUID.randomUUID();
        Marca marca = Marca.of(id, "Marca Teste", "Descrição da marca", null, null, null);

        when(repository.buscarPorId(id)).thenReturn(Optional.of(marca));

        // When
        Marca resultado = buscarMarcaPorIdUseCase.executar(id);

        // Then
        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
        assertEquals("Marca Teste", resultado.getNome());
        assertEquals("Descrição da marca", resultado.getDescricao().getValue());
        verify(repository).buscarPorId(id);
    }

    @Test
    @DisplayName("Deve lançar exceção quando marca não for encontrada")
    void deveLancarExcecaoQuandoMarcaNaoForEncontrada() {
        // Given
        UUID id = UUID.randomUUID();
        when(repository.buscarPorId(id)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, 
                () -> buscarMarcaPorIdUseCase.executar(id));
        
        assertEquals("Marca não encontrada com ID: " + id, exception.getMessage());
        verify(repository).buscarPorId(id);
    }

    @Test
    @DisplayName("Deve buscar marca com logo")
    void deveBuscarMarcaComLogo() {
        // Given
        UUID id = UUID.randomUUID();
        Marca marca = Marca.of(id, "Marca com Logo", "Descrição da marca", "http://logo.com/marca.jpg", null, null);

        when(repository.buscarPorId(id)).thenReturn(Optional.of(marca));

        // When
        Marca resultado = buscarMarcaPorIdUseCase.executar(id);

        // Then
        assertNotNull(resultado);
        assertEquals("http://logo.com/marca.jpg", resultado.getImagemUrl().getValue());
        verify(repository).buscarPorId(id);
    }
}
