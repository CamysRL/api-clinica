package br.edu.ifsp.clinica_api.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtFilter jwtFilter;
    private final JwtService jwtService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth

                        // Login público
                        .requestMatchers("/auth/login").permitAll()

                        // ------------------------------
                        //        CONSULTAS
                        // ------------------------------

                        // ADMIN & RECEPCIONISTA → acesso total às listagens
                        .requestMatchers(
                                "/consultas",
                                "/consultas/status/**",
                                "/consultas/periodo",
                                "/consultas/unidade/**"
                        ).hasAnyRole("ADMIN", "RECEPCIONISTA")

                        // Excluir TODAS as consultas → somente ADMIN
                        .requestMatchers(HttpMethod.DELETE, "/consultas")
                        .hasRole("ADMIN")

                        // ------------------------------------------------
                        // PACIENTE → pode acessar APENAS as próprias consultas
                        // ------------------------------------------------

                        // Ver suas consultas
                        .requestMatchers(HttpMethod.GET, "/consultas/paciente/**")
                        .hasRole("PACIENTE")

                        // Criar
                        .requestMatchers(HttpMethod.POST, "/consultas")
                        .hasAnyRole("PACIENTE", "RECEPCIONISTA", "ADMIN")

                        // Editar/Atualizar/Excluir (apenas as dele → validado no Service)
                        .requestMatchers(HttpMethod.PUT, "/consultas/**")
                        .hasAnyRole("PACIENTE", "RECEPCIONISTA", "ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/consultas/**")
                        .hasAnyRole("PACIENTE", "RECEPCIONISTA", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/consultas/**")
                        .hasAnyRole("PACIENTE", "RECEPCIONISTA", "ADMIN")

                        // ------------------------------------------------
                        // MÉDICO → pode ver APENAS as próprias consultas
                        // ------------------------------------------------

                        // Ver consultas dele
                        .requestMatchers(HttpMethod.GET, "/consultas/medico/**")
                        .hasRole("MEDICO")

                        // Atualizar status de suas consultas
                        .requestMatchers(HttpMethod.PATCH, "/consultas/**")
                        .hasRole("MEDICO")

                        // ------------------------------
                        // Qualquer outra rota → autenticado
                        // ------------------------------
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }


}
