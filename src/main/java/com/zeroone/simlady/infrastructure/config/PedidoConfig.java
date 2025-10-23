package com.zeroone.simlady.infrastructure.config;

import com.zeroone.simlady.core.application.usecases.pedido.*;
import com.zeroone.simlady.infrastructure.persistance.adapter.PedidoJpaAdapter;
import com.zeroone.simlady.infrastructure.persistance.adapter.ProdutoJpaAdapterPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PedidoConfig {

    @Bean
    public CriarPedidoUseCase criarPedidoUseCase(PedidoJpaAdapter adapter, ProdutoJpaAdapterPort produtoAdapter) {
        return new CriarPedidoUseCase(adapter, produtoAdapter);
    }

    @Bean
    public BuscarPedidoPorIdUseCase buscarPedidoPorIdUseCase(PedidoJpaAdapter adapter) {
        return new BuscarPedidoPorIdUseCase(adapter);
    }

    @Bean
    public AtualizarPedidoUseCase atualizarPedidoUseCase(PedidoJpaAdapter adapter) {
        return new AtualizarPedidoUseCase(adapter);
    }

    @Bean
    public DeletarPedidoPorIdUseCase deletarPedidoPorIdUseCase(PedidoJpaAdapter adapter) {
        return new DeletarPedidoPorIdUseCase(adapter);
    }

    @Bean
    public ListarPedidosUseCase listarPedidosUseCase(PedidoJpaAdapter adapter) {
        return new ListarPedidosUseCase(adapter);
    }

    @Bean
    public ListarPedidosPorStatusUseCase listarPedidosPorStatusUseCase(PedidoJpaAdapter adapter) {
        return new ListarPedidosPorStatusUseCase(adapter);
    }

    @Bean
    public AdicionarItemAoPedidoUseCase adicionarItemAoPedidoUseCase(PedidoJpaAdapter adapter) {
        return new AdicionarItemAoPedidoUseCase(adapter);
    }

    @Bean
    public AlterarStatusPedidoUseCase alterarStatusPedidoUseCase(PedidoJpaAdapter adapter) {
        return new AlterarStatusPedidoUseCase(adapter);
    }
}
