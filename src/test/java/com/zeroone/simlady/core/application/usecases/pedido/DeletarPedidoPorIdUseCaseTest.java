package com.zeroone.simlady.core.application.usecases.pedido;

import com.zeroone.simlady.core.application.ports.PedidoRepositoryPort;
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
class DeletarPedidoPorIdUseCaseTest {

    @Mock
    private PedidoRepositoryPort repository;

    @InjectMocks
    private DeletarPedidoPorIdUseCase deletarPedidoPorIdUseCase;

    @Test
    @DisplayName("Deve deletar pedido com sucesso")
    void deveDeletarPedidoComSucesso() {
        // Given
        UUID id = UUID.randomUUID();

        // When
        assertDoesNotThrow(() -> deletarPedidoPorIdUseCase.executar(id));

        // Then
        verify(repository).deletarPorId(id);
    }

    @Test
    @DisplayName("Deve deletar pedido com ID válido")
    void deveDeletarPedidoComIdValido() {
        // Given
        UUID id = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

        // When
        assertDoesNotThrow(() -> deletarPedidoPorIdUseCase.executar(id));

        // Then
        verify(repository).deletarPorId(id);
    }

    @Test
    @DisplayName("Deve executar deleção sem retorno")
    void deveExecutarDelecaoSemRetorno() {
        // Given
        UUID id = UUID.randomUUID();

        // When
        deletarPedidoPorIdUseCase.executar(id);

        // Then
        verify(repository, times(1)).deletarPorId(id);
        verifyNoMoreInteractions(repository);
    }
}
