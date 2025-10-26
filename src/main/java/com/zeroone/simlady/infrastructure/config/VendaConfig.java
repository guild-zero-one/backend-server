package com.zeroone.simlady.infrastructure.config;

import com.zeroone.simlady.core.application.usecases.venda.*;
import com.zeroone.simlady.infrastructure.persistance.adapter.VendaJpaAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VendaConfig {

    @Bean
    public CriarVendaUseCase criarVendaUseCase(VendaJpaAdapter adapter) {
        return new CriarVendaUseCase(adapter);
    }

    @Bean
    public BuscarVendaPorIdUseCase buscarVendaPorIdUseCase(VendaJpaAdapter adapter) {
        return new BuscarVendaPorIdUseCase(adapter);
    }

    @Bean
    public ListarVendasUseCase listarVendasUseCase(VendaJpaAdapter adapter) {
        return new ListarVendasUseCase(adapter);
    }

    @Bean
    public AtualizarVendaUseCase atualizarVendaUseCase(VendaJpaAdapter adapter) {
        return new AtualizarVendaUseCase(adapter);
    }

    @Bean
    public DeletarVendaPorIdUseCase deletarVendaPorIdUseCase(VendaJpaAdapter adapter) {
        return new DeletarVendaPorIdUseCase(adapter);
    }

    @Bean
    public ConfirmarPagamentoVendaUseCase confirmarPagamentoVendaUseCase(VendaJpaAdapter adapter) {
        return new ConfirmarPagamentoVendaUseCase(adapter);
    }
}
