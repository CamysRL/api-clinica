package br.edu.ifsp.clinica_api.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class TokenFilter extends OncePerRequestFilter {

    private static final String STATIC_AUTH_TOKEN = "token-secreto";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // Lê o header
        String token = request.getHeader("X-Auth-Token");

        // Se for uma requisição pública (ex: login), pode liberar
        String path = request.getRequestURI();
        if (path.startsWith("/public") || path.startsWith("/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Verifica o token
        if (token == null || !STATIC_AUTH_TOKEN.equals(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
            return; // Interrompe o fluxo
        }

        // Se o token for válido, segue o fluxo normal
        filterChain.doFilter(request, response);
    }
}

