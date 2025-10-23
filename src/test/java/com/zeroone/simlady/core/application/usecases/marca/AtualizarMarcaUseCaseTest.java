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
class AtualizarMarcaUseCaseTest {

    @Mock
    private MarcaRepositoryPort repository;

    @InjectMocks
    private AtualizarMarcaUseCase atualizarMarcaUseCase;

    @Test
    @DisplayName("Deve atualizar marca com sucesso")
    void deveAtualizarMarcaComSucesso() {
        // Given
        UUID id = UUID.randomUUID();
        Marca marca = Marca.of(id, "Marca Atualizada", "Nova descrição da marca", "http://logo.com/novo.jpg", null, null);

        Marca marcaAtualizada = Marca.of(id, "Marca Atualizada", "Nova descrição da marca", "http://logo.com/novo.jpg", null, null);

        when(repository.atualizarMarca(any(Marca.class))).thenReturn(marcaAtualizada);

        // When
        Marca resultado = atualizarMarcaUseCase.executar(marca);

        // Then
        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
        assertEquals("Marca Atualizada", resultado.getNome());
        assertEquals("Nova descrição da marca", resultado.getDescricao().getValue());
        assertEquals("http://logo.com/novo.jpg", resultado.getImagemUrl().getValue());
        verify(repository).atualizarMarca(marca);
    }

    @Test
    @DisplayName("Deve atualizar marca com apenas nome")
    void deveAtualizarMarcaComApenasNome() {
        // Given
        UUID id = UUID.randomUUID();
        Marca marca = Marca.of(id, "Marca Atualizada", null, null, null, null);

        when(repository.atualizarMarca(any(Marca.class))).thenReturn(marca);

        // When
        Marca resultado = atualizarMarcaUseCase.executar(marca);

        // Then
        assertNotNull(resultado);
        assertEquals("Marca Atualizada", resultado.getNome());
        verify(repository).atualizarMarca(marca);
    }

    @Test
    @DisplayName("Deve atualizar marca sem logo")
    void deveAtualizarMarcaSemLogo() {
        // Given
        UUID id = UUID.randomUUID();
        Marca marca = Marca.of(id, "Marca Atualizada", "Nova descrição", null, null, null);

        when(repository.atualizarMarca(any(Marca.class))).thenReturn(marca);

        // When
        Marca resultado = atualizarMarcaUseCase.executar(marca);

        // Then
        assertNotNull(resultado);
        assertEquals("Marca Atualizada", resultado.getNome());
        assertEquals("Nova descrição", resultado.getDescricao().getValue());
        assertNull(resultado.getImagemUrl());
        verify(repository).atualizarMarca(marca);
    }
}
