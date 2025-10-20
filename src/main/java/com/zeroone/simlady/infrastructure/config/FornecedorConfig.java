package com.zeroone.simlady.infrastructure.config;

import com.zeroone.simlady.core.application.usecases.fornecedor.*;
import com.zeroone.simlady.infrastructure.persistance.adapter.FornecedorJpaAdapter;
import com.zeroone.simlady.infrastructure.persistance.adapter.ProdutoJpaAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FornecedorConfig {

    @Bean
    public CriarFornecedorUseCase criarFornecedorUseCase (FornecedorJpaAdapter adapter){
        return new CriarFornecedorUseCase(adapter);
    }

    @Bean
    public AtualizarFornecedorUseCase atualizarFornecedorUseCase (FornecedorJpaAdapter adapter){
        return new AtualizarFornecedorUseCase(adapter);
    }

    @Bean
    public BuscarFornecedorPorIdUseCase buscarFornecedorPorIdUseCase (FornecedorJpaAdapter adapter){
        return new BuscarFornecedorPorIdUseCase(adapter);
    }

    @Bean
    public ListarFornecedorUseCase listarFornecedorUseCase (FornecedorJpaAdapter adapter){
        return new ListarFornecedorUseCase(adapter);
    }

    @Bean
    public ListarFornecedorComProdutosUseCase listarFornecedorComProdutosUseCase (FornecedorJpaAdapter adapter, ProdutoJpaAdapter produtoAdapter){
        return new ListarFornecedorComProdutosUseCase(adapter, produtoAdapter);
    }

    @Bean
    public DeletarFornecedorPorId deletarFornecedorPorId (FornecedorJpaAdapter adapter){
        return new DeletarFornecedorPorId(adapter);
    }


}
