package com.zeroone.simlady.config.security;

import com.zeroone.simlady.core.application.usecases.security.AutenticarUsuarioUseCase;
import com.zeroone.simlady.core.application.usecases.security.CarregarUsuarioPorUsernameUseCase;
import com.zeroone.simlady.core.application.usecases.security.ValidarTokenUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguracao {

    private final CarregarUsuarioPorUsernameUseCase carregarUsuarioPorUsernameUseCase;
    private final ValidarTokenUseCase validarTokenUseCase;

    private static final String[] SWAGGER_URLS = {
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/api/public/**",
            "/api/public/authenticate",
            "/webjars/**",
            "/v3/api-docs/**",
            "/actuator/*",
            "/h2-console/**"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .cors(Customizer.withDefaults())
                .csrf(CsrfConfigurer<HttpSecurity>::disable)
                .authorizeHttpRequests(authorize -> authorize
                                // Swagger and public endpoints
                                .requestMatchers(SWAGGER_URLS).permitAll()
                                // User registration and login
                                .requestMatchers(HttpMethod.POST, "/usuarios/login").permitAll()
                                .requestMatchers(HttpMethod.POST, "/usuarios").permitAll()
                                .requestMatchers(HttpMethod.POST, "/usuarios/logout").permitAll()
                                .requestMatchers(HttpMethod.POST, "/produtosCA").permitAll()
                                .requestMatchers(HttpMethod.GET, "/produtosCA").permitAll()
                                .requestMatchers("/**").hasAuthority("ADMIN")
                )
                .exceptionHandling(handling -> handling.authenticationEntryPoint(jwtAuthenticationEntryPointBean()))
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(jwtAuthenticationFilterBean(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(new AutenticacaoProvider(carregarUsuarioPorUsernameUseCase, passwordEncoder()));
        return authenticationManagerBuilder.build();
    }



    @Bean
    public AutenticacaoEntryPoint jwtAuthenticationEntryPointBean() {
        return new AutenticacaoEntryPoint();
    }

    @Bean
    public AutenticacaoFilter jwtAuthenticationFilterBean() {
        return new AutenticacaoFilter(carregarUsuarioPorUsernameUseCase, validarTokenUseCase, jwtAuthenticationUtilBean());
    }

    @Bean
    public GerenciadorTokenJwt jwtAuthenticationUtilBean() {
        return new GerenciadorTokenJwt();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
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
