package br.edu.ifsp.clinica_api.service;

import br.edu.ifsp.clinica_api.model.Usuario;
import br.edu.ifsp.clinica_api.model.enums.Papel;
import br.edu.ifsp.clinica_api.repository.UsuarioRepository;
import br.edu.ifsp.clinica_api.service.UsuarioService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private UsuarioService usuarioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // criarUsuario
    @Test
    void deveCriarUsuarioComCamposCorretos() {
        String email = "teste@teste.com";
        String senhaPura = "123456";
        Long idReferencia = 10L;
        Papel papel = Papel.PACIENTE;

        // Simula criptografia
        when(encoder.encode(senhaPura)).thenReturn("senhaCriptografada");

        // Simula retorno do repository
        when(usuarioRepository.save(any(Usuario.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Usuario resultado = usuarioService.criarUsuario(email, senhaPura, idReferencia, papel);

        // ----- Verificações -----
        assertNotNull(resultado);
        assertEquals(email, resultado.getEmail());
        assertEquals("senhaCriptografada", resultado.getSenha());
        assertTrue(resultado.isAtivo());
        assertEquals(idReferencia, resultado.getId_referencia());
        assertEquals(papel, resultado.getPapel());
        assertEquals(LocalDate.now(), resultado.getDataCriacao());

        // ----- Verifica se o encoder foi chamado -----
        verify(encoder, times(1)).encode(senhaPura);

        // ----- Verifica se salvou no repositório -----
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }
}

