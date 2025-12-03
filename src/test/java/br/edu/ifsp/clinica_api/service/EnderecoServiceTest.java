package br.edu.ifsp.clinica_api.service;

import br.edu.ifsp.clinica_api.dto.EnderecoDTO;
import br.edu.ifsp.clinica_api.model.Endereco;
import br.edu.ifsp.clinica_api.repository.EnderecoRepository;
import br.edu.ifsp.clinica_api.service.EnderecoService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EnderecoServiceTest {

    @Mock
    private EnderecoRepository enderecoRepository;

    @InjectMocks
    private EnderecoService enderecoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // TESTE: Buscar por ID
    @Test
    void deveRetornarEnderecoQuandoIdForInformado() {
        EnderecoDTO dto = new EnderecoDTO();
        dto.setId(10L);

        Endereco endereco = new Endereco();
        endereco.setId(10L);

        when(enderecoRepository.findById(10L)).thenReturn(Optional.of(endereco));

        Endereco resultado = enderecoService.getOrCreate(dto);

        assertNotNull(resultado);
        assertEquals(10L, resultado.getId());
        verify(enderecoRepository, times(1)).findById(10L);
        verify(enderecoRepository, never()).save(any());
    }

    // Buscar por dados quando endereço já existe
    @Test
    void deveRetornarEnderecoExistenteQuandoCamposForemIguais() {
        EnderecoDTO dto = new EnderecoDTO();
        dto.setLogradouro("Rua A");
        dto.setNumero(123);
        dto.setCidade("Cidade");
        dto.setEstado("SP");
        dto.setCep("00000-000");

        Endereco existente = new Endereco();
        existente.setId(5L);

        when(enderecoRepository.findByLogradouroAndNumeroAndCidadeAndEstadoAndCep(
                "Rua A", 123, "Cidade", "SP", "00000-000"
        )).thenReturn(Optional.of(existente));

        Endereco resultado = enderecoService.getOrCreate(dto);

        assertNotNull(resultado);
        assertEquals(5L, resultado.getId());

        verify(enderecoRepository, times(1))
                .findByLogradouroAndNumeroAndCidadeAndEstadoAndCep(
                        "Rua A", 123, "Cidade", "SP", "00000-000"
                );

        verify(enderecoRepository, never()).save(any());
    }

    // Criar novo endereço quando não existir
    @Test
    void deveCriarNovoEnderecoQuandoNaoExistir() {
        EnderecoDTO dto = new EnderecoDTO();
        dto.setLogradouro("Rua B");
        dto.setNumero(50);
        dto.setCidade("Cidade");
        dto.setEstado("RJ");
        dto.setCep("11111-111");
        dto.setBairro("Centro");
        dto.setComplemento("Apto 2");

        when(enderecoRepository.findByLogradouroAndNumeroAndCidadeAndEstadoAndCep(
                "Rua B", 50, "Cidade", "RJ", "11111-111"
        )).thenReturn(Optional.empty());

        Endereco salvo = new Endereco();
        salvo.setId(1L);

        when(enderecoRepository.save(any(Endereco.class))).thenReturn(salvo);

        Endereco resultado = enderecoService.getOrCreate(dto);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());

        verify(enderecoRepository, times(1))
                .findByLogradouroAndNumeroAndCidadeAndEstadoAndCep(
                        "Rua B", 50, "Cidade", "RJ", "11111-111"
                );

        verify(enderecoRepository, times(1)).save(any(Endereco.class));
    }
}

