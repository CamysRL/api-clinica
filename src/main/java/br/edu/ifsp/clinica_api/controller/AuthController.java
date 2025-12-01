package br.edu.ifsp.clinica_api.controller;

import br.edu.ifsp.clinica_api.dto.LoginRequest;
import br.edu.ifsp.clinica_api.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        String token = authService.login(request.getEmail(), request.getSenha());
        return ResponseEntity.ok().body(token);
    }
}

