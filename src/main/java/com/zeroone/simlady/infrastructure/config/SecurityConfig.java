package com.zeroone.simlady.infrastructure.config;

import com.zeroone.simlady.core.application.ports.TokenRepositoryPort;
import com.zeroone.simlady.infrastructure.security.AutenticarUsuarioSecurityUseCase;
import com.zeroone.simlady.infrastructure.security.CarregarUsuarioPorUsernameUseCase;
import com.zeroone.simlady.infrastructure.security.ExtrairIdUsuarioUseCase;
import com.zeroone.simlady.infrastructure.security.GerarTokenUseCase;
import com.zeroone.simlady.infrastructure.security.ValidarTokenUseCase;
import com.zeroone.simlady.infrastructure.security.JwtAuthenticationFilter;
import com.zeroone.simlady.infrastructure.adapters.AutenticacaoRepositoryAdapter;
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
    public AutenticacaoRepositoryAdapter autenticacaoAdapter(UsuarioJpaAdapter usuarioRepository, PasswordEncoder passwordEncoder) {
        return new AutenticacaoRepositoryAdapter(usuarioRepository, passwordEncoder);
    }

    @Bean
    public JwtTokenValidatorAdapter jwtTokenValidatorAdapter(TokenRepositoryPort tokenRepositoryPort) {
        return new JwtTokenValidatorAdapter(tokenRepositoryPort);
    }

    @Bean
    public ValidarTokenUseCase validarTokenUseCase(TokenRepositoryPort tokenRepositoryPort) {
        return new ValidarTokenUseCase(tokenRepositoryPort);
    }

    @Bean
    public AutenticarUsuarioSecurityUseCase autenticarUsuarioSecurityUseCase(AutenticacaoRepositoryAdapter autenticacaoAdapter) {
        return new AutenticarUsuarioSecurityUseCase(autenticacaoAdapter);
    }

    @Bean
    public CarregarUsuarioPorUsernameUseCase carregarUsuarioPorUsernameUseCase(AutenticacaoRepositoryAdapter autenticacaoAdapter) {
        return new CarregarUsuarioPorUsernameUseCase(autenticacaoAdapter);
    }

    @Bean
    public ExtrairIdUsuarioUseCase extrairIdUsuarioUseCase(TokenRepositoryPort tokenRepositoryPort) {
        return new ExtrairIdUsuarioUseCase(tokenRepositoryPort);
    }

    @Bean
    public GerarTokenUseCase gerarTokenUseCase(TokenRepositoryPort tokenRepositoryPort) {
        return new GerarTokenUseCase(tokenRepositoryPort);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .cors(Customizer.withDefaults())
                .csrf(CsrfConfigurer<HttpSecurity>::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(SWAGGER_URLS).permitAll()
                        .requestMatchers(HttpMethod.POST, "/usuarios").permitAll()
                        .requestMatchers(HttpMethod.POST, "/usuarios/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/usuarios/logout").permitAll()
                        .requestMatchers("/error").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuracao = new CorsConfiguration();

        configuracao.setAllowedOrigins(List.of("*"));
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
