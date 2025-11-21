package br.edu.ifsp.clinica_api.service;

import br.edu.ifsp.clinica_api.model.Usuario;
import br.edu.ifsp.clinica_api.repository.UsuarioRepository;
import br.edu.ifsp.clinica_api.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;

    public String login(String email, String senha) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, senha)
        );

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return jwtService.gerarToken(usuario);
    }
}
