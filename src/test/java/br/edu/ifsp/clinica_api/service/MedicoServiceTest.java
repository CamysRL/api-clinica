package br.edu.ifsp.clinica_api.service;

import br.edu.ifsp.clinica_api.dto.MedicoDTO;
import br.edu.ifsp.clinica_api.model.*;
import br.edu.ifsp.clinica_api.model.enums.EstadoCivil;
import br.edu.ifsp.clinica_api.model.enums.Genero;
import br.edu.ifsp.clinica_api.model.enums.Papel;
import br.edu.ifsp.clinica_api.model.enums.Sexo;
import br.edu.ifsp.clinica_api.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class MedicoServiceTest {

    @Mock
    private MedicoRepository medicoRepository;

    @Mock
    private EnderecoService enderecoService;

    @Mock
    private UnidadeRepository unidadeRepository;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private EspecialidadeRepository especialidadeRepository;

    @InjectMocks
    private MedicoService medicoService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveCadastrarMedicoComSucesso() {

        // DTO de entrada
        MedicoDTO dto = new MedicoDTO();
        dto.setNome("João da Silva");
        dto.setEmail("joao@teste.com");
        dto.setCelular("11999999999");
        dto.setCpf("12345678900");
        dto.setCrm("CRM123");
        dto.setRqe("RQE123");
        dto.setSenha("senha123");
        dto.setDataNascimento(LocalDate.of(1990, 1, 1));
        dto.setDataContratacao(LocalDate.of(2024, 1, 10));
        dto.setGenero(Genero.HOMEM);
        dto.setSexo(Sexo.MASCULINO);
        dto.setEstadoCivil(EstadoCivil.SOLTEIRO);

        dto.setUnidadeId(1L);
        dto.setEspecialidadeId(2L);

        Endereco endereco = new Endereco();
        endereco.setId(10L);

        Unidade unidade = new Unidade();
        unidade.setId(1L);

        Especialidade esp = new Especialidade();
        esp.setId_especialidade(2L);

        // Mockando dependências
        when(unidadeRepository.findById(1L)).thenReturn(Optional.of(unidade));
        when(especialidadeRepository.findById(2L)).thenReturn(Optional.of(esp));
        when(enderecoService.getOrCreate(any())).thenReturn(endereco);

        // Quando salvar o médico
        Medico medicoSalvo = new Medico();
        medicoSalvo.setId(100L);
        when(medicoRepository.save(any(Medico.class))).thenReturn(medicoSalvo);

        // Execução
        Medico resultado = medicoService.cadastrar(dto);

        // Verificações
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(100L);

        // Verifica se chamou o save()
        verify(medicoRepository, times(1)).save(any(Medico.class));

        // Verifica se criou usuario
        verify(usuarioService, times(1))
                .criarUsuario("joao@teste.com", "senha123", 100L, Papel.MEDICO);

        // Verifica se buscou corretamente unidade/especialidade
        verify(unidadeRepository).findById(1L);
        verify(especialidadeRepository).findById(2L);

        // Verifica endereço
        verify(enderecoService).getOrCreate(any());
    }

    @Test
    void deveListarTodosMedicos() {
        medicoService.listarTodos();
        verify(medicoRepository, times(1)).findAll();
    }

    @Test
    void deveExcluirMedicoComSucesso() {
        Long id = 10L;

        // Execução — não deve lançar exceção
        medicoService.excluir(id);

        // Verificação — deleteById deve ser chamado uma vez
        verify(medicoRepository, times(1)).deleteById(id);
    }
}
