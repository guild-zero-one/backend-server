package com.zeroone.simlady.core.application.usecases.marca;

import com.zeroone.simlady.core.application.ports.MarcaRepositoryPort;
import com.zeroone.simlady.core.domain.marca.Marca;
import org.springframework.data.domain.Page;

public class ListarMarcaUseCase {
    private final MarcaRepositoryPort repository;

    public ListarMarcaUseCase(MarcaRepositoryPort repository) {
        this.repository = repository;
    }

    public Page<Marca> executar(int pagina, int tamanho) {
        return repository.listarTodos(pagina, tamanho);
    }

}
