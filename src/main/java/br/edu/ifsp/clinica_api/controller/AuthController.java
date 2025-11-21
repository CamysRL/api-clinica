package br.edu.ifsp.clinica_api.controller;

import br.edu.ifsp.clinica_api.dto.UsuarioDTO;
import br.edu.ifsp.clinica_api.security.TokenResponse;
import br.edu.ifsp.clinica_api.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UsuarioDTO dto) {
        String token = authService.login(dto.getEmail(), dto.getSenha());
        return ResponseEntity.ok(new TokenResponse(token));
    }
}
