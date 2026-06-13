package br.edu.ifpb.es.daw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuração TEMPORÁRIA.
 *
 * Libera todos os endpoints enquanto a Issue 2 (JWT) e a Issue 3 (SecurityConfiguration final
 * com JwtAuthenticationFilter + UserDetailsService) ainda não estão prontas.
 *
 * Quando a Issue 3 estiver implementada, este arquivo deve ser SUBSTITUÍDO pela
 * SecurityConfiguration definitiva (não deixar as duas coexistirem).
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                );

        return http.build();
    }

}