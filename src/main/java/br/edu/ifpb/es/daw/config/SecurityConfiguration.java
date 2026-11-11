package br.edu.ifpb.es.daw.config;

import br.edu.ifpb.es.daw.rest.filter.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.io.IOException;
import java.time.LocalDateTime;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, proxyTargetClass = true)
public class SecurityConfiguration {

    @Autowired private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired private UserDetailsService userDetailsService;
    @Autowired private PasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/login", "/error", "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/clientes", "/vendedores").permitAll()
                        // Rotas "do usuário logado" que colidem com os padrões públicos abaixo.
                        .requestMatchers(HttpMethod.GET, "/produtos/meus", "/vendedores/me", "/avaliacoes/minhas").authenticated()
                        // Catálogo público (vitrine).
                        .requestMatchers(HttpMethod.GET,
                                "/produtos", "/produtos/buscar", "/produtos/{id}",
                                "/categorias", "/categorias/{id}",
                                "/vendedores/{id}").permitAll()
                        .requestMatchers(avaliacoesDeUmProduto()).permitAll()
                        .requestMatchers("/seed/**").hasAuthority("ADMIN")
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((req, res, err) -> escreverErro(res, 401, "NAO_AUTENTICADO",
                                "Você precisa estar autenticado para acessar este recurso. " +
                                "Faça login em /auth/login e envie o token no header: Authorization: Bearer <token>"))
                        .accessDeniedHandler((req, res, err) -> escreverErro(res, 403, "ACESSO_NEGADO",
                                "Você não tem permissão para realizar esta operação."))
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /** GET /avaliacoes?idProduto=... é público; a listagem geral (sem idProduto) continua restrita ao ADMIN. */
    private RequestMatcher avaliacoesDeUmProduto() {
        RequestMatcher getAvaliacoes = PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.GET, "/avaliacoes");
        return request -> getAvaliacoes.matches(request) && request.getParameter("idProduto") != null;
    }

    private void escreverErro(HttpServletResponse res, int status, String tipo, String detalhe) throws IOException {
        res.setStatus(status);
        res.setCharacterEncoding("UTF-8");
        res.setContentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE);
        res.getWriter().write(String.format(
                "{\"type\":\"%s\",\"status\":%d,\"detail\":\"%s\",\"timestamp\":\"%s\"}",
                tipo, status, detalhe, LocalDateTime.now()));
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }
}
