package br.edu.ifsp.clinica_api.service;

import br.edu.ifsp.clinica_api.dto.ConsultaCreateDTO;
import br.edu.ifsp.clinica_api.model.Consulta;
import br.edu.ifsp.clinica_api.model.Medico;
import br.edu.ifsp.clinica_api.model.Paciente;
import br.edu.ifsp.clinica_api.model.Unidade;
import br.edu.ifsp.clinica_api.model.enums.StatusConsulta;
import br.edu.ifsp.clinica_api.repository.ConsultaRepository;
import br.edu.ifsp.clinica_api.repository.MedicoRepository;
import br.edu.ifsp.clinica_api.repository.PacienteRepository;
import br.edu.ifsp.clinica_api.repository.UnidadeRepository;
import br.edu.ifsp.clinica_api.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ConsultaServiceTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private ConsultaRepository consultaRepository;

    @Mock
    private PacienteRepository pacienteRepository;

    @Mock
    private MedicoRepository medicoRepository;

    @Mock
    private UnidadeRepository unidadeRepository;

    @InjectMocks
    private ConsultaService consultaService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveCriarConsultaComSucesso() {
        ConsultaCreateDTO dto = new ConsultaCreateDTO();
        dto.setStatus("PENDENTE");
        dto.setDataConsulta("2025-10-12");
        dto.setHoraConsulta("14:30");
        dto.setPacienteId(1L);
        dto.setMedicoId(2L);
        dto.setUnidadeId(3L);

        Paciente paciente = new Paciente();
        paciente.setId(1L);

        Medico medico = new Medico();
        medico.setId(2L);

        Unidade unidade = new Unidade();
        unidade.setId(3L);

        Consulta consultaSalva = new Consulta();
        consultaSalva.setId(10L);

        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(medicoRepository.findById(2L)).thenReturn(Optional.of(medico));
        when(unidadeRepository.findById(3L)).thenReturn(Optional.of(unidade));
        when(consultaRepository.save(any())).thenReturn(consultaSalva);

        Consulta result = consultaService.createConsulta(dto);

        assertEquals(10L, result.getId());
        verify(consultaRepository, times(1)).save(any());
    }

    @Test
    void deveLancarErroQuandoPacienteNaoExiste() {
        ConsultaCreateDTO dto = new ConsultaCreateDTO();
        dto.setPacienteId(99L);

        when(pacienteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> consultaService.createConsulta(dto));
    }

    @Test
    void deveDeletarConsultaComPermissao() {
        when(jwtService.getPapelDoUsuarioLogado()).thenReturn("ADMIN");
        when(consultaRepository.existsById(1L)).thenReturn(true);

        consultaService.deleteConsulta(1L);

        verify(consultaRepository).deleteById(1L);
    }

    @Test
    void deveNegarAcessoAoDeletarConsulta() {
        when(jwtService.getPapelDoUsuarioLogado()).thenReturn("PACIENTE");

        assertThrows(AccessDeniedException.class,
                () -> consultaService.deleteConsulta(1L));
    }

    @Test
    void deveAtualizarStatusComSucesso() {
        Consulta consulta = new Consulta();
        consulta.setId(1L);
        consulta.setStatus(StatusConsulta.PENDENTE);

        when(consultaRepository.findById(1L)).thenReturn(Optional.of(consulta));
        when(consultaRepository.save(any())).thenReturn(consulta);

        Consulta result = consultaService.updateStatus(1L, "CONFIRMADA");

        assertEquals(StatusConsulta.CONFIRMADA, result.getStatus());
    }

    @Test
    void deveLancarErroParaStatusInvalido() {
        assertThrows(IllegalArgumentException.class,
                () -> consultaService.updateStatus(1L, "INVALIDO"));
    }

    @Test
    void medicoPodeVerSuasConsultas() {
        when(jwtService.getPapelDoUsuarioLogado()).thenReturn("MEDICO");
        when(jwtService.getIdReferenciaDoUsuarioLogado()).thenReturn(5L);

        consultaService.getAllConsultasPorMedico(5L);

        verify(consultaRepository).findByMedicoId(5L);
    }

    @Test
    void medicoNaoPodeVerConsultasDeOutroMedico() {
        when(jwtService.getPapelDoUsuarioLogado()).thenReturn("MEDICO");
        when(jwtService.getIdReferenciaDoUsuarioLogado()).thenReturn(5L);

        assertThrows(AccessDeniedException.class,
                () -> consultaService.getAllConsultasPorMedico(8L));
    }
}

