package br.edu.ifsp.clinica_api.service;

import br.edu.ifsp.clinica_api.dto.RecepcionistaDTO;
import br.edu.ifsp.clinica_api.model.Endereco;
import br.edu.ifsp.clinica_api.model.Recepcionista;
import br.edu.ifsp.clinica_api.model.Unidade;
import br.edu.ifsp.clinica_api.model.enums.EstadoCivil;
import br.edu.ifsp.clinica_api.model.enums.Genero;
import br.edu.ifsp.clinica_api.model.enums.Papel;
import br.edu.ifsp.clinica_api.model.enums.Sexo;
import br.edu.ifsp.clinica_api.repository.RecepcionistaRepository;
import br.edu.ifsp.clinica_api.repository.UnidadeRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class RecepcionistaServiceTest {

    @Mock
    private RecepcionistaRepository recepcionistaRepository;

    @Mock
    private EnderecoService enderecoService;

    @Mock
    private UnidadeRepository unidadeRepository;

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private RecepcionistaService recepcionistaService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // -------------------------------------------------------------------------
    // LISTAR TODOS
    // -------------------------------------------------------------------------
    @Test
    void deveListarTodosRecepcionistas() {
        recepcionistaService.listarTodos();
        verify(recepcionistaRepository, times(1)).findAll();
    }

    // -------------------------------------------------------------------------
    // CADASTRAR
    // -------------------------------------------------------------------------
    @Test
    void deveCadastrarRecepcionistaComSucesso() {

        // DTO de entrada
        RecepcionistaDTO dto = new RecepcionistaDTO();
        dto.setNome("Ana Silva");
        dto.setEmail("ana@teste.com");
        dto.setCelular("11999999999");
        dto.setCpf("12345678900");
        dto.setSenha("senha123");
        dto.setDataNascimento(LocalDate.of(1992, 5, 10));
        dto.setDataContratacao(LocalDate.of(2024, 2, 1));
        dto.setGenero(Genero.MULHER);
        dto.setSexo(Sexo.FEMININO);
        dto.setEstadoCivil(EstadoCivil.SOLTEIRO);
        dto.setUnidadeId(1L);

        Endereco endereco = new Endereco();
        endereco.setId(3L);

        Unidade unidade = new Unidade();
        unidade.setId(1L);

        // Mock para unidade
        when(unidadeRepository.findById(1L)).thenReturn(Optional.of(unidade));

        // Mock para endereço
        when(enderecoService.getOrCreate(any())).thenReturn(endereco);

        // Mock para recepcionista salvo
        Recepcionista salvo = new Recepcionista();
        salvo.setId(88L);

        when(recepcionistaRepository.save(any(Recepcionista.class))).thenReturn(salvo);

        // Execução
        Recepcionista resultado = recepcionistaService.cadastrar(dto);

        // Asserts
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(88L);

        // Verifica unidade
        verify(unidadeRepository, times(1)).findById(1L);

        // Verifica endereço
        verify(enderecoService, times(1)).getOrCreate(any());

        // Verifica save()
        verify(recepcionistaRepository, times(1)).save(any(Recepcionista.class));

        // Verifica criação do usuário
        verify(usuarioService, times(1))
                .criarUsuario("ana@teste.com", "senha123", 88L, Papel.RECEPCIONISTA);
    }

    @Test
    void deveExcluirRecepcionistaComSucesso() {
        Long id = 50L;

        recepcionistaService.excluir(id);

        verify(recepcionistaRepository, times(1)).deleteById(id);
    }
}
