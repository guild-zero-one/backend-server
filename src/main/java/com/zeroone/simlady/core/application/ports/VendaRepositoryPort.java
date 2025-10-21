package com.zeroone.simlady.core.application.ports;

import com.zeroone.simlady.core.domain.venda.Venda;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface VendaRepositoryPort {
    Venda salvarVenda(Venda venda);
    Optional<Venda> buscarPorId(UUID id);
    void deletarPorId(UUID id);
    Venda atualizarVenda(Venda venda);
    Page<Venda> listarTodas(int pagina, int tamanho);
    Page<Venda> listarPorDataVenda(LocalDate dataInicio, LocalDate dataFim, int pagina, int tamanho);
    Page<Venda> listarPorPagamentoRealizado(Boolean pagamentoRealizado, int pagina, int tamanho);
}
