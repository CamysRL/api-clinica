package br.edu.ifsp.clinica_api.service;

import br.edu.ifsp.clinica_api.dto.PacienteDTO;
import br.edu.ifsp.clinica_api.model.Endereco;
import br.edu.ifsp.clinica_api.model.Paciente;
import br.edu.ifsp.clinica_api.model.enums.EstadoCivil;
import br.edu.ifsp.clinica_api.model.enums.Genero;
import br.edu.ifsp.clinica_api.model.enums.Papel;
import br.edu.ifsp.clinica_api.model.enums.Sexo;
import br.edu.ifsp.clinica_api.repository.PacienteRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class PacienteServiceTest {

    @Mock
    private PacienteRepository pacienteRepository;

    @Mock
    private EnderecoService enderecoService;

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private PacienteService pacienteService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveListarTodosPacientes() {
        pacienteService.listarTodos();
        verify(pacienteRepository, times(1)).findAll();
    }

    @Test
    void deveCadastrarPacienteComSucesso() {

        // DTO de entrada
        PacienteDTO dto = new PacienteDTO();
        dto.setNome("Maria Souza");
        dto.setEmail("maria@teste.com");
        dto.setCelular("11988888888");
        dto.setCpf("98765432100");
        dto.setSenha("senha123");
        dto.setDataNascimento(LocalDate.of(1995, 3, 20));
        dto.setGenero(Genero.MULHER);
        dto.setSexo(Sexo.FEMININO);
        dto.setEstadoCivil(EstadoCivil.SOLTEIRO);

        Endereco endereco = new Endereco();
        endereco.setId(10L);

        // Mock do endereço
        when(enderecoService.getOrCreate(any())).thenReturn(endereco);

        // Mock do paciente salvo
        Paciente pacienteSalvo = new Paciente();
        pacienteSalvo.setId(200L);
        when(pacienteRepository.save(any(Paciente.class))).thenReturn(pacienteSalvo);

        // Execução
        Paciente resultado = pacienteService.cadastrar(dto);

        // Verificações
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(200L);

        // Verifica chamada do save()
        verify(pacienteRepository, times(1)).save(any(Paciente.class));

        // Verifica endereço
        verify(enderecoService, times(1)).getOrCreate(any());

        // Verifica criação do usuário
        verify(usuarioService, times(1))
                .criarUsuario("maria@teste.com", "senha123", 200L, Papel.PACIENTE);
    }

    @Test
    void deveExcluirPacienteComSucesso() {
        Long id = 10L;

        // Execução — não deve lançar exceção
        pacienteService.excluir(id);

        // Verificação — deleteById deve ser chamado uma vez
        verify(pacienteRepository, times(1)).deleteById(id);
    }

}