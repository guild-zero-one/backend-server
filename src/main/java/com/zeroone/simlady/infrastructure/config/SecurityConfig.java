package com.zeroone.simlady.infrastructure.config;

import com.zeroone.simlady.core.application.usecases.security.AutenticarUsuarioSecurityUseCase;
import com.zeroone.simlady.core.application.usecases.security.CarregarUsuarioPorUsernameUseCase;
import com.zeroone.simlady.core.application.usecases.security.ExtrairIdUsuarioUseCase;
import com.zeroone.simlady.core.application.usecases.security.GerarTokenUseCase;
import com.zeroone.simlady.core.application.usecases.security.ValidarTokenUseCase;
import com.zeroone.simlady.infrastructure.adapters.AutenticacaoAdapter;
import com.zeroone.simlady.infrastructure.adapters.JwtTokenValidatorAdapter;
import com.zeroone.simlady.infrastructure.persistance.adapter.UsuarioJpaAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private static final String[] SWAGGER_URLS = {
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/v3/api-docs/**",
            "/webjars/**",
            "/actuator/**",
            "/h2-console/**"
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AutenticacaoAdapter autenticacaoAdapter(UsuarioJpaAdapter usuarioRepository, PasswordEncoder passwordEncoder) {
        return new AutenticacaoAdapter(usuarioRepository, passwordEncoder);
    }

    @Bean
    public JwtTokenValidatorAdapter jwtTokenValidatorAdapter() {
        return new JwtTokenValidatorAdapter();
    }

    @Bean
    public ValidarTokenUseCase validarTokenUseCase(JwtTokenValidatorAdapter validatorAdapter) {
        return new ValidarTokenUseCase(validatorAdapter);
    }

    @Bean
    public AutenticarUsuarioSecurityUseCase autenticarUsuarioSecurityUseCase(AutenticacaoAdapter autenticacaoAdapter) {
        return new AutenticarUsuarioSecurityUseCase(autenticacaoAdapter);
    }

    @Bean
    public CarregarUsuarioPorUsernameUseCase carregarUsuarioPorUsernameUseCase(AutenticacaoAdapter autenticacaoAdapter) {
        return new CarregarUsuarioPorUsernameUseCase(autenticacaoAdapter);
    }

    @Bean
    public ExtrairIdUsuarioUseCase extrairIdUsuarioUseCase(com.zeroone.simlady.core.domain.services.TokenExtractionService tokenExtractionService) {
        return new ExtrairIdUsuarioUseCase(tokenExtractionService);
    }

    @Bean
    public GerarTokenUseCase gerarTokenUseCase(com.zeroone.simlady.core.domain.services.TokenGenerationService tokenGenerationService) {
        return new GerarTokenUseCase(tokenGenerationService);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .cors(Customizer.withDefaults())
                .csrf(CsrfConfigurer<HttpSecurity>::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(SWAGGER_URLS).permitAll()
                        .requestMatchers(HttpMethod.POST, "/usuariosCA").permitAll()
                        .requestMatchers(HttpMethod.POST, "/usuariosCA/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/usuariosCA/logout").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuracao = new CorsConfiguration();

        configuracao.setAllowedOrigins(List.of("http://localhost:3000"));
        configuracao.setAllowedMethods(Arrays.asList(
                HttpMethod.GET.name(),
                HttpMethod.POST.name(),
                HttpMethod.PUT.name(),
                HttpMethod.PATCH.name(),
                HttpMethod.DELETE.name(),
                HttpMethod.OPTIONS.name()
        ));
        configuracao.setAllowedHeaders(List.of("*"));
        configuracao.setAllowCredentials(true);
        configuracao.setExposedHeaders(List.of(HttpHeaders.CONTENT_DISPOSITION));

        UrlBasedCorsConfigurationSource origem = new UrlBasedCorsConfigurationSource();
        origem.registerCorsConfiguration("/**", configuracao);

        return origem;
    }
}
