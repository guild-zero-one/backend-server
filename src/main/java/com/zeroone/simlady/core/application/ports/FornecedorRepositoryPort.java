package com.zeroone.simlady.core.application.ports;

import com.zeroone.simlady.core.domain.fornecedor.Fornecedor;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.UUID;

public interface FornecedorRepositoryPort {
    Fornecedor salvarFornecedor(Fornecedor fornecedor);
    Optional<Fornecedor> buscarPorId(UUID id);
    void deletarPorId(UUID id);
    Fornecedor atualizarFornecedor(Fornecedor fornecedor);
    Page<Fornecedor> listarTodos(int pagina, int tamanho);
    Page<Fornecedor> listarComProdutos(int pagina, int tamanho);
}
