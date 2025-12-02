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

                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/pacientes").permitAll()

                        // CRIAÇÃO DE CONSULTA → permite PACIENTE, RECEPCIONISTA, ADMIN
                        .requestMatchers(HttpMethod.POST, "/consultas")
                        .hasAnyRole("PACIENTE", "RECEPCIONISTA", "ADMIN")

                        // CONSULTAS GERAIS (GET)
                        .requestMatchers(HttpMethod.GET, "/consultas")
                        .hasAnyRole("ADMIN", "RECEPCIONISTA")

                        // OUTROS FILTROS QUE SÃO GET
                        .requestMatchers(
                                "/consultas/status/**",
                                "/consultas/periodo",
                                "/consultas/unidade/**"
                        ).hasAnyRole("ADMIN", "RECEPCIONISTA")

                        // DELETE sem ID → só admin
                        .requestMatchers(HttpMethod.DELETE, "/consultas")
                        .hasRole("ADMIN")

                        // ROTAS QUE USAM ID
                        .requestMatchers("/consultas/**")
                        .hasAnyRole("PACIENTE", "RECEPCIONISTA", "ADMIN")

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
