package com.zeroone.simlady.core.application.usecases.fornecedor;

import com.zeroone.simlady.core.application.ports.FornecedorRepositoryPort;
import com.zeroone.simlady.core.domain.fornecedor.Fornecedor;
import org.springframework.data.domain.Page;

public class ListarFornecedorUseCase {
    private final FornecedorRepositoryPort repository;

    public ListarFornecedorUseCase(FornecedorRepositoryPort repository) {
        this.repository = repository;
    }

    public Page<Fornecedor> executar(int pagina, int tamanho) {
        return repository.listarTodos(pagina, tamanho);
    }

}
