package com.zeroone.simlady.core.application.ports;

import com.zeroone.simlady.core.domain.marca.Marca;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.UUID;

public interface MarcaRepositoryPort {
    Marca salvarMarca(Marca marca);
    Optional<Marca> buscarPorId(UUID id);
    void deletarPorId(UUID id);
    Marca atualizarMarca(Marca marca);
    Page<Marca> listarTodos(int pagina, int tamanho);
    Page<Marca> listarComProdutos(int pagina, int tamanho);
}
