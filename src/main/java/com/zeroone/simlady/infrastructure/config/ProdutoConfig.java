package com.zeroone.simlady.infrastructure.config;

import com.zeroone.simlady.core.application.usecases.produto.*;
import com.zeroone.simlady.infrastructure.persistance.adapter.ProdutoJpaAdapterPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProdutoConfig {

    @Bean
    public CriarProdutoUseCase criarProdutoUseCase(ProdutoJpaAdapterPort adapter) {
        return new CriarProdutoUseCase(adapter);
    }

    @Bean
    public BuscarProdutoPorIdUseCase buscarProdutoPorIdUseCase(ProdutoJpaAdapterPort adapter) {
        return new BuscarProdutoPorIdUseCase(adapter);
    }

    @Bean
    public ListarProdutoUseCase listarProdutoUseCase(ProdutoJpaAdapterPort adapter) {
        return new ListarProdutoUseCase(adapter);
    }

    @Bean
    public AtualizarProdutoUseCase atualizarProdutoUseCase(ProdutoJpaAdapterPort adapter) {
        return new AtualizarProdutoUseCase(adapter);
    }

    @Bean
    public DeletarProdutoPorIdUseCase deletarProdutoPorIdUseCase(ProdutoJpaAdapterPort adapter) {
        return new DeletarProdutoPorIdUseCase(adapter);
    }

    @Bean
    public BuscarProdutoPorSkuUseCase buscarProdutoPorSkuUseCase(ProdutoJpaAdapterPort adapter) {
        return new BuscarProdutoPorSkuUseCase(adapter);
    }

    @Bean
    public ListarProdutosPorMarcaUseCase listarProdutosPorMarcaUseCase(ProdutoJpaAdapterPort adapter) {
        return new ListarProdutosPorMarcaUseCase(adapter);
    }

    @Bean
    public ListarProdutosPorCatalogoUseCase listarProdutosPorCatalogoUseCase(ProdutoJpaAdapterPort adapter) {
        return new ListarProdutosPorCatalogoUseCase(adapter);
    }

    @Bean
    public AlterarStatusCatalogoUseCase alterarStatusCatalogoUseCase(ProdutoJpaAdapterPort adapter) {
        return new AlterarStatusCatalogoUseCase(adapter);
    }
}
