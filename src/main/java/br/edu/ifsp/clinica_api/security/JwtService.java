package br.edu.ifsp.clinica_api.security;

import br.edu.ifsp.clinica_api.model.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    private final long expirationMs = 86400000; // 24 horas

    // ----------- GERAR TOKEN -----------
    public String gerarToken(Usuario usuario) {
        return Jwts.builder()
                .setSubject(usuario.getEmail())
                .claim("id", usuario.getId())
                .claim("papel", usuario.getPapel().name())
                .claim("idRef", usuario.getId_referencia())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes())
                .compact();
    }

    // ----------- EXTRAIR CLAIMS -----------
    public Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey.getBytes())
                .parseClaimsJws(token)
                .getBody();
    }

    // ----------- PEGAR TOKEN DA REQUISIÇÃO -----------
    private String getTokenFromRequest() {
        ServletRequestAttributes attrs =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attrs == null) return null;

        HttpServletRequest request = attrs.getRequest();
        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }

        return null;
    }

    // ----------- OBTER PAPEL DO USUÁRIO LOGADO -----------
    public String getPapelDoUsuarioLogado() {
        String token = getTokenFromRequest();
        if (token == null) return null;

        Claims claims = extractClaims(token);
        return claims.get("papel", String.class);
    }

    // ----------- OBTER id_referencia DO USUÁRIO LOGADO -----------
    public Long getIdReferenciaDoUsuarioLogado() {
        String token = getTokenFromRequest();
        if (token == null) return null;

        Claims claims = extractClaims(token);
        return claims.get("idRef", Long.class);
    }

    // ----------- VALIDAÇÃO -----------
    public boolean tokenValido(String token, Usuario usuario) {
        String email = extractClaims(token).getSubject();
        return email.equals(usuario.getEmail()) &&
                !extractClaims(token).getExpiration().before(new Date());
    }
}



