package br.edu.ifsp.clinica_api.service;

import br.edu.ifsp.clinica_api.exceptions.ConsultaNotFoundException;
import br.edu.ifsp.clinica_api.model.Consulta;
import br.edu.ifsp.clinica_api.model.enums.StatusConsulta;
import br.edu.ifsp.clinica_api.repository.ConsultaRepository;
import br.edu.ifsp.clinica_api.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ConsultaService {

    private final JwtService jwtService;
    private final ConsultaRepository consultaRepository;

    // Criar consulta
    public Consulta createConsulta(Consulta newConsulta) {
        newConsulta.setDataHoraRegistrada(LocalDateTime.now());
        return consultaRepository.save(newConsulta);
    }

    // Listar tudo
    public List<Consulta> getAllConsultas() {
        return consultaRepository.findAll();
    }

    // Buscar por ID
    public Consulta getConsultaById(long id) {
        return consultaRepository.findById(id)
                .orElseThrow(() -> new ConsultaNotFoundException(id));
    }

    // Deletar uma
    public void deleteConsulta(long id) {

        String papel = jwtService.getPapelDoUsuarioLogado();

        if (!papel.equals("ADMIN") && !papel.equals("RECEPCIONISTA")) {
            throw new AccessDeniedException("Você não tem acesso");
        }

        if (!consultaRepository.existsById(id))
            throw new ConsultaNotFoundException(id);

        consultaRepository.deleteById(id);
    }

    // Deletar tudo
    public void deleteAllConsultas() {
        consultaRepository.deleteAll();
    }

    // Atualizar consulta inteira
    public Consulta updateConsulta(long id, Consulta updatedConsulta) {
        Consulta original = consultaRepository.findById(id)
                .orElseThrow(() -> new ConsultaNotFoundException(id));

        updatedConsulta.setId(id);
        updatedConsulta.setDataHoraRegistrada(original.getDataHoraRegistrada());

        return consultaRepository.save(updatedConsulta);
    }

    // Atualizar apenas status
    public Consulta updateStatus(long id, String status) {

        StatusConsulta novoStatus;

        try {
            novoStatus = StatusConsulta.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Status inválido. Use: PENDENTE, CANCELADA, CONFIRMADA, CONCLUIDA, AUSENTE."
            );
        }

        Consulta consulta = consultaRepository.findById(id)
                .orElseThrow(() -> new ConsultaNotFoundException(id));

        consulta.setStatus(novoStatus);

        return consultaRepository.save(consulta);
    }

    // Filtrar por médico
    public List<Consulta> getAllConsultasPorMedico(long idMedico) {

        String papel = jwtService.getPapelDoUsuarioLogado();
        Long idRef = jwtService.getIdReferenciaDoUsuarioLogado();

        if (papel.equals("PACIENTE")) {
            throw new AccessDeniedException("Você não tem acesso a essas informações");
        }

        if (papel.equals("MEDICO") && idMedico != idRef) {
            throw new AccessDeniedException("Você só pode acessar suas próprias consultas.");
        }

        return consultaRepository.findByMedicoId(idMedico);
    }

    // Filtrar por paciente
    public List<Consulta> getAllConsultasPorPaciente(long idPaciente) {
        String papel = jwtService.getPapelDoUsuarioLogado();
        Long idRef = jwtService.getIdReferenciaDoUsuarioLogado();

        if (papel.equals("MEDICO")) {
            throw new AccessDeniedException("Você não tem acesso a essas informações");
        }

        if (papel.equals("PACIENTE") && idPaciente != idRef) {
            throw new AccessDeniedException("Você não tem acesso a essas informações");
        }

        return consultaRepository.findByPacienteId(idPaciente);
    }

    // Filtrar por unidade
    public List<Consulta> getAllConsultasPorUnidade(long idClinica) {
        String papel = jwtService.getPapelDoUsuarioLogado();

        if (!papel.equals("ADMIN") && !papel.equals("RECEPCIONISTA")) {
            throw new AccessDeniedException("Você não tem acesso a essas informações");
        }

        return consultaRepository.findByUnidadeId(idClinica);
    }

    // Filtrar por status
    public List<Consulta> getAllConsultasPorStatus(String status) {
        String papel = jwtService.getPapelDoUsuarioLogado();

        if (!papel.equals("ADMIN") && !papel.equals("RECEPCIONISTA")) {
            throw new AccessDeniedException("Você não tem acesso a essas informações");
        }

        try {
            StatusConsulta.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Status inválido: " + status);
        }

        return consultaRepository.findByStatus(status);
    }

    // Filtrar por período
    public List<Consulta> getAllConsultasPorPeriodo(LocalDate inicio, LocalDate fim) {
        String papel = jwtService.getPapelDoUsuarioLogado();

        if (!papel.equals("ADMIN") && !papel.equals("RECEPCIONISTA")) {
            throw new AccessDeniedException("Você não tem acesso a essas informações");
        }

        return consultaRepository.findByDataConsultaBetween(inicio, fim);
    }

    // Médico + período
    public List<Consulta> getAllConsultasPorMedicoEPeriodo(long idMedico, LocalDate inicio, LocalDate fim) {
        String papel = jwtService.getPapelDoUsuarioLogado();
        Long idRef = jwtService.getIdReferenciaDoUsuarioLogado();

        if (papel.equals("PACIENTE")) {
            throw new AccessDeniedException("Você não tem acesso a essas informações");
        }

        if (papel.equals("MEDICO") && idMedico != idRef) {
            throw new AccessDeniedException("Você não tem acesso a essas informações");
        }

        return consultaRepository.findByMedico_IdAndDataConsultaBetween(idMedico, inicio, fim);
    }

    // Unidade + período
    public List<Consulta> getAllConsultasPorUnidadeEPeriodo(long idUnidade, LocalDate inicio, LocalDate fim) {
        String papel = jwtService.getPapelDoUsuarioLogado();

        if (!papel.equals("ADMIN") && !papel.equals("RECEPCIONISTA")) {
            throw new AccessDeniedException("Você não tem acesso a essas informações");
        }

        return consultaRepository.findByUnidade_IdAndDataConsultaBetween(idUnidade, inicio, fim);
    }

    // Paciente + período
    public List<Consulta> getAllConsultasPorPacienteEPeriodo(long idPaciente, LocalDate inicio, LocalDate fim) {
        String papel = jwtService.getPapelDoUsuarioLogado();
        Long idRef = jwtService.getIdReferenciaDoUsuarioLogado();

        if (papel.equals("MEDICO")) {
            throw new AccessDeniedException("Você não tem acesso a essas informações");
        }

        if (papel.equals("PACIENTE") && idPaciente != idRef) {
            throw new AccessDeniedException("Você não tem acesso a essas informações");
        }

        return consultaRepository.findByPaciente_IdAndDataConsultaBetween(idPaciente, inicio, fim);
    }
}
