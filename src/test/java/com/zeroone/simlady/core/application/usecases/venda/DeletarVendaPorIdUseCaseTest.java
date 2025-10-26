package com.zeroone.simlady.core.application.usecases.venda;

import com.zeroone.simlady.core.application.ports.VendaRepositoryPort;
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
class DeletarVendaPorIdUseCaseTest {

    @Mock
    private VendaRepositoryPort repository;

    @InjectMocks
    private DeletarVendaPorIdUseCase deletarVendaPorIdUseCase;

    @Test
    @DisplayName("Deve deletar venda com sucesso")
    void deveDeletarVendaComSucesso() {
        // Given
        UUID id = UUID.randomUUID();

        // When
        assertDoesNotThrow(() -> deletarVendaPorIdUseCase.executar(id));

        // Then
        verify(repository).deletarPorId(id);
    }

    @Test
    @DisplayName("Deve deletar venda com ID válido")
    void deveDeletarVendaComIdValido() {
        // Given
        UUID id = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

        // When
        assertDoesNotThrow(() -> deletarVendaPorIdUseCase.executar(id));

        // Then
        verify(repository).deletarPorId(id);
    }

    @Test
    @DisplayName("Deve executar deleção sem retorno")
    void deveExecutarDelecaoSemRetorno() {
        // Given
        UUID id = UUID.randomUUID();

        // When
        deletarVendaPorIdUseCase.executar(id);

        // Then
        verify(repository, times(1)).deletarPorId(id);
        verifyNoMoreInteractions(repository);
    }
}
