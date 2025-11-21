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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
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
        String papel = jwtService.getPapel();

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

        // Mantém campos importantes
        updatedConsulta.setId(id);
        updatedConsulta.setDataHoraRegistrada(original.getDataHoraRegistrada());

        return consultaRepository.save(updatedConsulta);
    }

    // Atualizar APENAS status
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

        String papel = jwtService.getPapel();
        Long idRef = jwtService.getIdReferencia(); // id do médico logado

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
        String papel = jwtService.getPapel();
        Long idRef = jwtService.getIdReferencia(); // id do paciente logado

        if (papel.equals("MEDICO")) {
            throw new AccessDeniedException("Você não tem acesso a essas informações");
        }

        if (papel.equals("PACIENTE") && idPaciente != idRef) {
            throw new AccessDeniedException("Você não tem acesso a essas informações");
        }

        return consultaRepository.findByPacienteId(idPaciente);
    }

    // Filtrar por clínica
    public List<Consulta> getAllConsultasPorUnidade(long idClinica) {
        String papel = jwtService.getPapel();

        if (!papel.equals("ADMIN") && !papel.equals("RECEPCIONISTA")) {
            throw new AccessDeniedException("Você não tem acesso a essas informações");
        }

        return consultaRepository.findByUnidadeId(idClinica);
    }

    // Filtrar por status
    public List<Consulta> getAllConsultasPorStatus(String status) {
        String papel = jwtService.getPapel();

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

    // Filtrar por período (data início e fim)
    public List<Consulta> getAllConsultasPorPeriodo(LocalDate inicio, LocalDate fim) {
        String papel = jwtService.getPapel();

        if (!papel.equals("ADMIN") && !papel.equals("RECEPCIONISTA")) {
            throw new AccessDeniedException("Você não tem acesso a essas informações");
        }

        return consultaRepository.findByDataConsultaBetween(inicio, fim);
    }

    // Médico + período
    public List<Consulta> getAllConsultasPorMedicoEPeriodo(long idMedico, LocalDate inicio, LocalDate fim) {
        String papel = jwtService.getPapel();
        Long idRef = jwtService.getIdReferencia(); // id do médico logado

        if (papel.equals("PACIENTE")) {
            throw new AccessDeniedException("Você não tem acesso a essas informações");
        }

        if (papel.equals("MEDICO") && idMedico != idRef) {
            throw new AccessDeniedException("Você não tem acesso a essas informações");
        }

        return consultaRepository.findByDataConsultaMedicoBetween(idMedico, inicio, fim);
    }

    // Clínica + período
    public List<Consulta> getAllConsultasPorUnidadeEPeriodo(long idUnidade, LocalDate inicio, LocalDate fim) {
        String papel = jwtService.getPapel();

        if (!papel.equals("ADMIN") && !papel.equals("RECEPCIONISTA")) {
            throw new AccessDeniedException("Você só pode acessar suas próprias consultas.");
        }

        return consultaRepository.findByDataConsultaUnidadeBetween(idUnidade, inicio, fim);
    }

    // Paciente + período
    public List<Consulta> getAllConsultasPorPacienteEPeriodo(long idPaciente, LocalDate inicio, LocalDate fim) {
        String papel = jwtService.getPapel();
        Long idRef = jwtService.getIdReferencia(); // id do paciente logado

        if (papel.equals("MEDICO")) {
            throw new AccessDeniedException("Você não tem acesso a essas informações");
        }

        if (papel.equals("PACIENTE") && idPaciente != idRef) {
            throw new AccessDeniedException("Você não tem acesso a essas informações");
        }

        return consultaRepository.findByDataConsultaPacienteBetween(idPaciente, inicio, fim);
    }
}

