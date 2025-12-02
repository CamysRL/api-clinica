package br.edu.ifsp.clinica_api.service;

import br.edu.ifsp.clinica_api.model.Usuario;
import br.edu.ifsp.clinica_api.repository.UsuarioRepository;
import br.edu.ifsp.clinica_api.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveFazerLoginComSucesso() {
        String email = "teste@exemplo.com";
        String senha = "123456";

        Usuario usuario = new Usuario();
        usuario.setEmail(email);

        String tokenGerado = "token_jwt";

        // Mock do retorno da autenticação
        Authentication auth = mock(Authentication.class);
        when(authenticationManager.authenticate(any()))
                .thenReturn(auth);

        when(usuarioRepository.findByEmail(email))
                .thenReturn(Optional.of(usuario));

        when(jwtService.gerarToken(usuario))
                .thenReturn(tokenGerado);

        // Execução
        String token = authService.login(email, senha);

        // Asserts
        assertEquals(tokenGerado, token);
        verify(authenticationManager, times(1)).authenticate(any());
        verify(usuarioRepository, times(1)).findByEmail(email);
        verify(jwtService, times(1)).gerarToken(usuario);
    }


    @Test
    void deveLancarExcecaoQuandoUsuarioNaoEncontrado() {
        String email = "inexistente@teste.com";
        String senha = "123";

        when(usuarioRepository.findByEmail(email)).thenReturn(java.util.Optional.empty());

        // Assert que lança exceção
        assertThrows(RuntimeException.class, () -> authService.login(email, senha));

        verify(usuarioRepository, times(1)).findByEmail(email);
        verify(jwtService, never()).gerarToken(any());
    }
}