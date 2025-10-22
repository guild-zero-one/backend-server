package com.zeroone.simlady.infrastructure.config;

import com.zeroone.simlady.core.application.usecases.usuario.*;
import com.zeroone.simlady.infrastructure.adapters.JwtTokenAdapter;
import com.zeroone.simlady.infrastructure.adapters.JwtTokenExtractorAdapter;
import com.zeroone.simlady.infrastructure.persistance.adapter.UsuarioJpaAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UsuarioConfig {

    @Bean
    public CadastrarUsuarioUseCase cadastrarUsuarioUseCase(UsuarioJpaAdapter adapter) {
        return new CadastrarUsuarioUseCase(adapter);
    }

    @Bean
    public BuscarUsuarioPorIdUseCase buscarUsuarioPorIdUseCase(UsuarioJpaAdapter adapter) {
        return new BuscarUsuarioPorIdUseCase(adapter);
    }

    @Bean
    public BuscarUsuarioPorEmailUseCase buscarUsuarioPorEmailUseCase(UsuarioJpaAdapter adapter) {
        return new BuscarUsuarioPorEmailUseCase(adapter);
    }

    @Bean
    public ListarUsuariosUseCase listarUsuariosUseCase(UsuarioJpaAdapter adapter) {
        return new ListarUsuariosUseCase(adapter);
    }

    @Bean
    public ListarClientesUseCase listarClientesUseCase(UsuarioJpaAdapter adapter) {
        return new ListarClientesUseCase(adapter);
    }

    @Bean
    public AtualizarUsuarioUseCase atualizarUsuarioUseCase(UsuarioJpaAdapter adapter) {
        return new AtualizarUsuarioUseCase(adapter);
    }

    @Bean
    public DesativarUsuarioUseCase desativarUsuarioUseCase(UsuarioJpaAdapter adapter) {
        return new DesativarUsuarioUseCase(adapter);
    }

    @Bean
    public DeletarUsuarioUseCase deletarUsuarioUseCase(UsuarioJpaAdapter adapter) {
        return new DeletarUsuarioUseCase(adapter);
    }

    @Bean
    public AutenticarUsuarioUseCase autenticarUsuarioUseCase(UsuarioJpaAdapter adapter, JwtTokenAdapter jwtAdapter) {
        return new AutenticarUsuarioUseCase(adapter, jwtAdapter);
    }

    @Bean
    public BuscarUsuarioAutenticadoUseCase buscarUsuarioAutenticadoUseCase(UsuarioJpaAdapter adapter, JwtTokenExtractorAdapter jwtExtractor) {
        return new BuscarUsuarioAutenticadoUseCase(adapter, jwtExtractor);
    }
}
