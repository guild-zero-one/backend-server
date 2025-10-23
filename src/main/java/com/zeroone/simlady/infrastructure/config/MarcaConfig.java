package com.zeroone.simlady.infrastructure.config;

import com.zeroone.simlady.core.application.usecases.marca.*;
import com.zeroone.simlady.infrastructure.persistance.adapter.MarcaJpaAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MarcaConfig {

    @Bean
    public CriarMarcaUseCase criarMarcaUseCase (MarcaJpaAdapter adapter){
        return new CriarMarcaUseCase(adapter);
    }

    @Bean
    public AtualizarMarcaUseCase atualizarMarcaUseCase (MarcaJpaAdapter adapter){
        return new AtualizarMarcaUseCase(adapter);
    }

    @Bean
    public BuscarMarcaPorIdUseCase buscarMarcaPorIdUseCase (MarcaJpaAdapter adapter){
        return new BuscarMarcaPorIdUseCase(adapter);
    }

    @Bean
    public ListarMarcaUseCase listarMarcaUseCase (MarcaJpaAdapter adapter){
        return new ListarMarcaUseCase(adapter);
    }

    @Bean
    public DeletarMarcaPorIdUseCase deletarMarcaPorId (MarcaJpaAdapter adapter){
        return new DeletarMarcaPorIdUseCase(adapter);
    }

}
