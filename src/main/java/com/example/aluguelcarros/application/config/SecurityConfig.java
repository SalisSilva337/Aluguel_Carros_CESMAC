package com.example.aluguelcarros.application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
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

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private JwtTokenFilter jwtTokenFilter;

    public SecurityConfig(JwtTokenFilter jwtTokenFilter) {
        this.jwtTokenFilter = jwtTokenFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Configuração da porcaria do CORS
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // Rotas públicas
                .requestMatchers(
                    HttpMethod.POST, 
                    "/usuarios", 
                    "/usuarios/login"
                ).permitAll()
                .requestMatchers(
                    HttpMethod.GET, 
                    "/carros", 
                    "/carros/disponiveis"
                ).permitAll()
                .requestMatchers(
                    "/h2-console/**"
                ).permitAll()
                
                // Rotas de aluguéis
                .requestMatchers(HttpMethod.GET, "/alugueis/meus-alugueis").authenticated()
                .requestMatchers(HttpMethod.POST, "/alugueis/alugar").authenticated()
                .requestMatchers(HttpMethod.PUT, "/alugueis/devolver/**").authenticated()
                .requestMatchers(HttpMethod.GET, "/alugueis/usuario/**").authenticated()
                
                // Rotas dos adms do zap
                .requestMatchers(HttpMethod.POST, "/carros").authenticated()
                .requestMatchers(HttpMethod.PUT, "/carros/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/carros/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/usuarios").hasRole("ADMIN")
                
                .anyRequest().authenticated()
            )
            .csrf(csrf -> csrf
                .ignoringRequestMatchers(
                    "/h2-console/**",
                    "/usuarios",
                    "/usuarios/login"
                )
                .disable()
            )
            .headers(headers -> headers
                .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class); 
        
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000")); // url do front-end pro menino salis botar
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}