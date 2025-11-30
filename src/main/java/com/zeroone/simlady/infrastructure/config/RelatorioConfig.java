package com.zeroone.simlady.infrastructure.config;

import com.zeroone.simlady.core.application.usecases.relatorio.*;
import com.zeroone.simlady.infrastructure.persistance.adapter.RelatorioJpaAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RelatorioConfig {

    @Bean
    public ListarProdutosMaisVendidosUseCase listarProdutosMaisVendidosUseCase(RelatorioJpaAdapter adapter) {
        return new ListarProdutosMaisVendidosUseCase(adapter);
    }

    @Bean
    public ListarTop3ProdutosMaisVendidosMesAtualUseCase listarTop3ProdutosMaisVendidosMesAtualUseCase(RelatorioJpaAdapter adapter) {
        return new ListarTop3ProdutosMaisVendidosMesAtualUseCase(adapter);
    }

    @Bean
    public ObterResumoVendasProdutoUseCase obterResumoVendasProdutoUseCase(RelatorioJpaAdapter adapter) {
        return new ObterResumoVendasProdutoUseCase(adapter);
    }

    @Bean
    public CalcularTotalVendasMesAtualUseCase calcularTotalVendasMesAtualUseCase(RelatorioJpaAdapter adapter) {
        return new CalcularTotalVendasMesAtualUseCase(adapter);
    }

    @Bean
    public BuscarTop3ProdutosMaisVendidosMesAtualUseCase buscarTop3ProdutosMaisVendidosMesAtualUseCase(RelatorioJpaAdapter adapter) {
        return new BuscarTop3ProdutosMaisVendidosMesAtualUseCase(adapter);
    }

    @Bean
    public ObterQuantidadePedidosUltimos6MesesUseCase obterQuantidadePedidosUltimos6MesesUseCase(RelatorioJpaAdapter adapter) {
        return new ObterQuantidadePedidosUltimos6MesesUseCase(adapter);
    }

    @Bean
    public ObterFaturamentoUltimos6MesesUseCase obterFaturamentoUltimos6MesesUseCase(RelatorioJpaAdapter adapter) {
        return new ObterFaturamentoUltimos6MesesUseCase(adapter);
    }

    @Bean
    public ObterFaturamentoUltimos4MesesUseCase obterFaturamentoUltimos4MesesUseCase(RelatorioJpaAdapter adapter) {
        return new ObterFaturamentoUltimos4MesesUseCase(adapter);
    }

    @Bean
    public ObterPedidosPorStatusMesAtualUseCase obterPedidosPorStatusMesAtualUseCase(RelatorioJpaAdapter adapter) {
        return new ObterPedidosPorStatusMesAtualUseCase(adapter);
    }

    @Bean
    public ContarPedidosEmAbertoUseCase contarPedidosEmAbertoUseCase(RelatorioJpaAdapter adapter) {
        return new ContarPedidosEmAbertoUseCase(adapter);
    }
}
