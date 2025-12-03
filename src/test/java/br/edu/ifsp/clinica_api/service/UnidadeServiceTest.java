package br.edu.ifsp.clinica_api.service;

import br.edu.ifsp.clinica_api.model.Unidade;
import br.edu.ifsp.clinica_api.repository.UnidadeRepository;
import br.edu.ifsp.clinica_api.service.UnidadeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UnidadeServiceTest {

    @Mock
    private UnidadeRepository unidadeRepository;

    @InjectMocks
    private UnidadeService unidadeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // listarTodos()
    @Test
    void deveListarTodasUnidades() {
        Unidade u1 = new Unidade();
        u1.setId(1L);
        u1.setNome("Unidade Central");

        Unidade u2 = new Unidade();
        u2.setId(2L);
        u2.setNome("Unidade Sul");

        when(unidadeRepository.findAll()).thenReturn(Arrays.asList(u1, u2));

        List<Unidade> unidades = unidadeService.listarTodos();

        assertEquals(2, unidades.size());
        assertEquals("Unidade Central", unidades.get(0).getNome());
        assertEquals("Unidade Sul", unidades.get(1).getNome());

        verify(unidadeRepository, times(1)).findAll();
    }

    // cadastrar
    @Test
    void deveCadastrarUnidade() {
        Unidade unidade = new Unidade();
        unidade.setNome("Unidade Norte");

        when(unidadeRepository.save(unidade)).thenReturn(unidade);

        Unidade resultado = unidadeService.cadastrar(unidade);

        assertNotNull(resultado);
        assertEquals("Unidade Norte", resultado.getNome());

        verify(unidadeRepository, times(1)).save(unidade);
    }

    //excluir
    @Test
    void deveExcluirUnidade() {
        Long id = 5L;

        doNothing().when(unidadeRepository).deleteById(id);

        unidadeService.excluir(id);

        verify(unidadeRepository, times(1)).deleteById(id);
    }
}