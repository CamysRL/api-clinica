package br.edu.ifsp.clinica_api.service;

import br.edu.ifsp.clinica_api.exceptions.ConsultaNotFoundException;
import br.edu.ifsp.clinica_api.model.Consulta;
import br.edu.ifsp.clinica_api.model.enums.StatusConsulta;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.time.LocalDate;

@Service
public class ConsultaService {

    private final List<Consulta> consultas = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong();

    // Criar consulta
    public Consulta createConsulta(Consulta newConsulta) {
        newConsulta.setId(idGenerator.incrementAndGet());
        newConsulta.setDataHoraRegistrada(LocalDateTime.now());
        consultas.add(newConsulta);
        return newConsulta;
    }

    // Listar tudo
    public List<Consulta> getAllConsultas() {
        return consultas;
    }

    // Buscar por ID
    public Consulta getConsultaById(long id) {
        return consultas.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElseThrow(() -> new ConsultaNotFoundException(id));
    }

    // Deletar uma
    public void deleteConsulta(long id) {
        boolean removed = consultas.removeIf(c -> c.getId().equals(id));
        if (!removed) throw new ConsultaNotFoundException(id);
    }

    // Deletar tudo
    public void deleteAllConsultas() {
        consultas.clear();
    }

    // Atualizar consulta inteira
    public Consulta updateConsulta(long id, Consulta updatedConsulta) {
        Consulta original = getConsultaById(id);

        updatedConsulta.setId(id);
        // Mantém a data de registro original
        updatedConsulta.setDataHoraRegistrada(original.getDataHoraRegistrada());

        consultas.remove(original);
        consultas.add(updatedConsulta);
        return updatedConsulta;
    }

    // Atualizar APENAS status
    public Consulta updateStatus(long id, String status) {

        StatusConsulta novoStatus;

        try {
            novoStatus = StatusConsulta.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Status inválido. Use: PENDENTE, CANCELADA, REALIZADA, REMARCADA."
            );
        }

        Consulta consulta = getConsultaById(id);
        consulta.setStatus(novoStatus);
        return consulta;
    }

    // Filtrar por médico
    public List<Consulta> getAllConsultasPorMedico(long idMedico) {
        return consultas.stream()
                .filter(c -> c.getMedico() != null && c.getMedico().getId() == idMedico)
                .toList();
    }

    // Filtrar por paciente
    public List<Consulta> getAllConsultasPorPaciente(long idPaciente) {
        return consultas.stream()
                .filter(c -> c.getPaciente() != null && c.getPaciente().getId() == idPaciente)
                .toList();
    }

    // Filtrar por clínica
    public List<Consulta> getAllConsultasPorUnidade(long idClinica) {
        return consultas.stream()
                .filter(c -> c.getUnidade() != null && c.getUnidade().getId() == idClinica)
                .toList();
    }

    // Filtrar por status
    public List<Consulta> getAllConsultasPorStatus(String status) {
        try {
            StatusConsulta statusEnum = StatusConsulta.valueOf(status.toUpperCase());
            return consultas.stream()
                    .filter(c -> c.getStatus() == statusEnum)
                    .toList();
        } catch (Exception e) {
            throw new IllegalArgumentException("Status inválido.");
        }
    }

    // Filtrar por período (data início e fim)
    public List<Consulta> getAllConsultasPorPeriodo(LocalDate inicio, LocalDate fim) {
        return consultas.stream()
                .filter(c -> c.getDataConsulta() != null &&
                        !c.getDataConsulta().isBefore(inicio) &&
                        !c.getDataConsulta().isAfter(fim))
                .toList();
    }

    // Médico + período
    public List<Consulta> getAllConsultasPorMedicoEPeriodo(long idMedico, LocalDate inicio, LocalDate fim) {
        return consultas.stream()
                .filter(c -> c.getMedico() != null &&
                        c.getMedico().getId() == idMedico &&
                        c.getDataConsulta() != null &&
                        !c.getDataConsulta().isBefore(inicio) &&
                        !c.getDataConsulta().isAfter(fim))
                .toList();
    }

    // Clínica + período
    public List<Consulta> getAllConsultasPorUnidadeEPeriodo(long idClinica, LocalDate inicio, LocalDate fim) {
        return consultas.stream()
                .filter(c -> c.getUnidade() != null &&
                        c.getUnidade().getId() == idClinica &&
                        c.getDataConsulta() != null &&
                        !c.getDataConsulta().isBefore(inicio) &&
                        !c.getDataConsulta().isAfter(fim))
                .toList();
    }

    // Paciente + período
    public List<Consulta> getAllConsultasPorPacienteEPeriodo(long idPaciente, LocalDate inicio, LocalDate fim) {
        return consultas.stream()
                .filter(c -> c.getPaciente() != null &&
                        c.getPaciente().getId() == idPaciente &&
                        c.getDataConsulta() != null &&
                        !c.getDataConsulta().isBefore(inicio) &&
                        !c.getDataConsulta().isAfter(fim))
                .toList();
    }
}

