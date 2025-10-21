package com.zeroone.simlady.infrastructure.config;

import com.zeroone.simlady.core.application.usecases.produto.*;
import com.zeroone.simlady.infrastructure.persistance.adapter.ProdutoJpaAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProdutoConfig {

    @Bean
    public CriarProdutoUseCase criarProdutoUseCase(ProdutoJpaAdapter adapter) {
        return new CriarProdutoUseCase(adapter);
    }

    @Bean
    public BuscarProdutoPorIdUseCase buscarProdutoPorIdUseCase(ProdutoJpaAdapter adapter) {
        return new BuscarProdutoPorIdUseCase(adapter);
    }

    @Bean
    public ListarProdutoUseCase listarProdutoUseCase(ProdutoJpaAdapter adapter) {
        return new ListarProdutoUseCase(adapter);
    }

    @Bean
    public AtualizarProdutoUseCase atualizarProdutoUseCase(ProdutoJpaAdapter adapter) {
        return new AtualizarProdutoUseCase(adapter);
    }

    @Bean
    public DeletarProdutoPorIdUseCase deletarProdutoPorIdUseCase(ProdutoJpaAdapter adapter) {
        return new DeletarProdutoPorIdUseCase(adapter);
    }

    @Bean
    public BuscarProdutoPorSkuUseCase buscarProdutoPorSkuUseCase(ProdutoJpaAdapter adapter) {
        return new BuscarProdutoPorSkuUseCase(adapter);
    }

    @Bean
    public ListarProdutosPorFornecedorUseCase listarProdutosPorFornecedorUseCase(ProdutoJpaAdapter adapter) {
        return new ListarProdutosPorFornecedorUseCase(adapter);
    }

    @Bean
    public ListarProdutosPorCatalogoUseCase listarProdutosPorCatalogoUseCase(ProdutoJpaAdapter adapter) {
        return new ListarProdutosPorCatalogoUseCase(adapter);
    }

    @Bean
    public AlterarStatusCatalogoUseCase alterarStatusCatalogoUseCase(ProdutoJpaAdapter adapter) {
        return new AlterarStatusCatalogoUseCase(adapter);
    }
}
