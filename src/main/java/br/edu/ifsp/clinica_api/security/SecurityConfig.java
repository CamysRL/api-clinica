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

                        // ------------------------------
                        // LOGIN → totalmente público
                        // ------------------------------
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()

                        // ------------------------------
                        // CONSULTAS → ADMIN & RECEPCIONISTA
                        // ------------------------------
                        .requestMatchers(
                                "/consultas",
                                "/consultas/status/**",
                                "/consultas/periodo",
                                "/consultas/unidade/**"
                        ).hasAnyRole("ADMIN", "RECEPCIONISTA")

                        // Deletar TODAS as consultas → somente ADMIN
                        .requestMatchers(HttpMethod.DELETE, "/consultas")
                        .hasRole("ADMIN")

                        // ------------------------------
                        // CONSULTAS DO PACIENTE
                        // ------------------------------
                        .requestMatchers(HttpMethod.GET, "/consultas/paciente/**")
                        .hasRole("PACIENTE")

                        // Criar consulta → paciente, recepcionista e admin
                        .requestMatchers(HttpMethod.POST, "/consultas")
                        .hasAnyRole("PACIENTE", "RECEPCIONISTA", "ADMIN")

                        // Atualizar / alterar / excluir → apenas autorizado no service
                        .requestMatchers(HttpMethod.PUT, "/consultas/**")
                        .hasAnyRole("PACIENTE", "RECEPCIONISTA", "ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/consultas/**")
                        .hasAnyRole("PACIENTE", "RECEPCIONISTA", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/consultas/**")
                        .hasAnyRole("PACIENTE", "RECEPCIONISTA", "ADMIN")

                        // ------------------------------
                        // CONSULTAS DO MÉDICO
                        // ------------------------------
                        .requestMatchers(HttpMethod.GET, "/consultas/medico/**")
                        .hasRole("MEDICO")

                        // Atualizar status (somente das dele → validado no service)
                        .requestMatchers(HttpMethod.PATCH, "/consultas/medico/**")
                        .hasRole("MEDICO")

                        // ------------------------------
                        // Qualquer outra rota → precisa estar autenticado
                        // ------------------------------
                        .anyRequest().authenticated()
                )

                // ------------------------------
                // FILTRO JWT
                // ------------------------------
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
