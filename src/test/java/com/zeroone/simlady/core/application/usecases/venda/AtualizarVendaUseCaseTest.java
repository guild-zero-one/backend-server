package com.zeroone.simlady.core.application.usecases.venda;

import com.zeroone.simlady.core.application.ports.VendaRepositoryPort;
import com.zeroone.simlady.core.domain.venda.Venda;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AtualizarVendaUseCaseTest {

    @Mock
    private VendaRepositoryPort repository;

    @InjectMocks
    private AtualizarVendaUseCase atualizarVendaUseCase;

    @Test
    @DisplayName("Deve atualizar venda com sucesso")
    void deveAtualizarVendaComSucesso() {
        // Given
        UUID id = UUID.randomUUID();
        String novoValorTotal = "1500.0";
        String novoDesconto = "150.0";
        LocalDate novaDataVenda = LocalDate.now().plusDays(1);
        List<UUID> novosPedidosIds = List.of(UUID.randomUUID(), UUID.randomUUID());
        
        Venda vendaExistente = Venda.of(
                id,
                com.zeroone.simlady.core.domain.venda.vendaVOs.ValorTotal.of("1000.0"),
                com.zeroone.simlady.core.domain.venda.vendaVOs.Desconto.of("100.0"),
                false,
                LocalDate.now(),
                new java.util.ArrayList<>(),
                java.time.LocalDateTime.now(),
                java.time.LocalDateTime.now()
        );
        
        Venda vendaAtualizada = Venda.of(
                id,
                com.zeroone.simlady.core.domain.venda.vendaVOs.ValorTotal.of(novoValorTotal),
                com.zeroone.simlady.core.domain.venda.vendaVOs.Desconto.of(novoDesconto),
                false,
                novaDataVenda,
                new java.util.ArrayList<>(),
                java.time.LocalDateTime.now(),
                java.time.LocalDateTime.now()
        );

        when(repository.buscarPorId(id)).thenReturn(Optional.of(vendaExistente));
        when(repository.atualizarVenda(any(Venda.class))).thenReturn(vendaAtualizada);

        // When
        Optional<Venda> resultado = atualizarVendaUseCase.executar(id, novoValorTotal, novoDesconto, novaDataVenda, novosPedidosIds);

        // Then
        assertTrue(resultado.isPresent());
        assertEquals(id, resultado.get().getId());
        verify(repository).buscarPorId(id);
        verify(repository).atualizarVenda(any(Venda.class));
    }

    @Test
    @DisplayName("Deve retornar Optional vazio quando venda não for encontrada")
    void deveRetornarOptionalVazioQuandoVendaNaoForEncontrada() {
        // Given
        UUID id = UUID.randomUUID();
        when(repository.buscarPorId(id)).thenReturn(Optional.empty());

        // When
        Optional<Venda> resultado = atualizarVendaUseCase.executar(id, "1000.0", "100.0", LocalDate.now(), List.of());

        // Then
        assertTrue(resultado.isEmpty());
        verify(repository).buscarPorId(id);
        verify(repository, never()).atualizarVenda(any(Venda.class));
    }

    @Test
    @DisplayName("Deve atualizar apenas valor total")
    void deveAtualizarApenasValorTotal() {
        // Given
        UUID id = UUID.randomUUID();
        String novoValorTotal = "2000.0";
        
        Venda vendaExistente = Venda.of(
                id,
                com.zeroone.simlady.core.domain.venda.vendaVOs.ValorTotal.of("1000.0"),
                com.zeroone.simlady.core.domain.venda.vendaVOs.Desconto.of("100.0"),
                false,
                LocalDate.now(),
                new java.util.ArrayList<>(),
                java.time.LocalDateTime.now(),
                java.time.LocalDateTime.now()
        );
        
        Venda vendaAtualizada = Venda.of(
                id,
                com.zeroone.simlady.core.domain.venda.vendaVOs.ValorTotal.of(novoValorTotal),
                com.zeroone.simlady.core.domain.venda.vendaVOs.Desconto.of("100.0"),
                false,
                LocalDate.now(),
                new java.util.ArrayList<>(),
                java.time.LocalDateTime.now(),
                java.time.LocalDateTime.now()
        );

        when(repository.buscarPorId(id)).thenReturn(Optional.of(vendaExistente));
        when(repository.atualizarVenda(any(Venda.class))).thenReturn(vendaAtualizada);

        // When
        Optional<Venda> resultado = atualizarVendaUseCase.executar(id, novoValorTotal, null, null, null);

        // Then
        assertTrue(resultado.isPresent());
        verify(repository).buscarPorId(id);
        verify(repository).atualizarVenda(any(Venda.class));
    }

    @Test
    @DisplayName("Deve atualizar apenas desconto")
    void deveAtualizarApenasDesconto() {
        // Given
        UUID id = UUID.randomUUID();
        String novoDesconto = "200.0";
        
        Venda vendaExistente = Venda.of(
                id,
                com.zeroone.simlady.core.domain.venda.vendaVOs.ValorTotal.of("1000.0"),
                com.zeroone.simlady.core.domain.venda.vendaVOs.Desconto.of("100.0"),
                false,
                LocalDate.now(),
                new java.util.ArrayList<>(),
                java.time.LocalDateTime.now(),
                java.time.LocalDateTime.now()
        );
        
        Venda vendaAtualizada = Venda.of(
                id,
                com.zeroone.simlady.core.domain.venda.vendaVOs.ValorTotal.of("1000.0"),
                com.zeroone.simlady.core.domain.venda.vendaVOs.Desconto.of(novoDesconto),
                false,
                LocalDate.now(),
                new java.util.ArrayList<>(),
                java.time.LocalDateTime.now(),
                java.time.LocalDateTime.now()
        );

        when(repository.buscarPorId(id)).thenReturn(Optional.of(vendaExistente));
        when(repository.atualizarVenda(any(Venda.class))).thenReturn(vendaAtualizada);

        // When
        Optional<Venda> resultado = atualizarVendaUseCase.executar(id, null, novoDesconto, null, null);

        // Then
        assertTrue(resultado.isPresent());
        verify(repository).buscarPorId(id);
        verify(repository).atualizarVenda(any(Venda.class));
    }
}
