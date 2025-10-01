package com.zeroone.simlady.infrastructure.config;

import com.zeroone.simlady.core.application.usecases.produto.CreateProdutoUseCase;
import com.zeroone.simlady.core.application.usecases.produto.ListProdutoUseCase;
import com.zeroone.simlady.infrastructure.persistance.adapter.ProdutoJpaAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProdutoConfig {

    @Bean
    public CreateProdutoUseCase createProdutoUseCase (ProdutoJpaAdapter adapter){
        return new CreateProdutoUseCase(adapter);
    }

    @Bean
    public ListProdutoUseCase listProdutoUseCase(ProdutoJpaAdapter adapter){
        return new ListProdutoUseCase(adapter);
    }
}
