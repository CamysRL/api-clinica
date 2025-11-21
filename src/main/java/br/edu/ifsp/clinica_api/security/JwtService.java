package br.edu.ifsp.clinica_api.security;

import br.edu.ifsp.clinica_api.model.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    private final long expirationMs = 86400000; // 24 horas

    // ----------- GERAR TOKEN -----------
    public String gerarToken(Usuario usuario) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("papel", usuario.getPapel().name());
        claims.put("id", usuario.getId());
        claims.put("idReferencia", usuario.getId_referencia());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(usuario.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes())
                .compact();
    }

    // ----------- EXTRATORES -----------
    public String getEmail() {
        return extractAllClaims(getToken()).getSubject();
    }

    public String getPapel() {
        return (String) extractAllClaims(getToken()).get("papel");
    }

    public Long getIdReferencia() {
        return ((Number) extractAllClaims(getToken()).get("idReferencia")).longValue();
    }

    // ----------- TOKEN UTIL -----------
    private String getToken() {
        var auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || auth.getCredentials() == null) {
            throw new RuntimeException("Token não encontrado na requisição.");
        }

        String authHeader = auth.getCredentials().toString();

        return authHeader.replace("Bearer ", "");
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey.getBytes())
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean tokenValido(String token, Usuario usuario) {
        final String email = extractAllClaims(token).getSubject();
        return (email.equals(usuario.getEmail()) && !extractAllClaims(token)
                .getExpiration().before(new Date()));
    }
}

