package br.edu.ifsp.clinica_api.service;

import br.edu.ifsp.clinica_api.exceptions.ConsultaNotFoundException;
import br.edu.ifsp.clinica_api.model.Consulta;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class ClinicaService {
    private final List<Consulta> consultas = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong();

    public Consulta createConsulta(Consulta newConsulta) {
        newConsulta.setId(idGenerator.incrementAndGet());
        consultas.add(newConsulta);
        return newConsulta;
    }

    public List<Consulta> getAllConsultas() {
        return consultas;
    }

    public Consulta getConsultaById(long id) {
        return consultas.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElseThrow(() -> new ConsultaNotFoundException(id));
    }

    public void deleteConsulta(long id) {
        boolean removed = consultas.removeIf(c -> c.getId() == id);
        if (!removed) throw new ConsultaNotFoundException(id);
    }

    public void deleteAllConsultas() {
        consultas.clear();
    }

    public Consulta updateStatus(long id, String status) {
        Consulta consulta = getConsultaById(id);
        consulta.setStatus_consulta(status);
        return consulta;
    }

    public Consulta updateConsulta(long id, Consulta updatedConsulta) {
        Consulta consulta = getConsultaById(id);
        updatedConsulta.setId(id);
        consultas.remove(consulta);
        consultas.add(updatedConsulta);
        return updatedConsulta;
    }

    public List<Consulta> getAllConsultasPorMedico (long idMedico) {
        List<Consulta> resultado = new ArrayList<>();
        for (Consulta consulta : consultas) {
            if (consulta.getMedico() != null && idMedico == consulta.getMedico().getId_medico()) {
                resultado.add(consulta);
            }
        }
        return resultado;
    }

    public List<Consulta> getAllConsultasPorPaciente (long idPaciente) {
        List<Consulta> resultado = new ArrayList<>();
        for (Consulta consulta : consultas) {
            if (consulta.getPaciente() != null && idPaciente == consulta.getPaciente().getId_paciente()) {
                resultado.add(consulta);
            }
        }
        return resultado;
    }

    public List<Consulta> getAllConsultasPorClinica (long idClinica) {
        List<Consulta> resultado = new ArrayList<>();
        for (Consulta consulta : consultas) {
            if (consulta.getClinica() != null && idClinica == consulta.getClinica().getId_clinica()) {
                resultado.add(consulta);
            }
        }
        return resultado;
    }

    public List<Consulta> getAllConsultasPorStatus (String status) {
        List<Consulta> resultado = new ArrayList<>();
        for (Consulta consulta : consultas) {
            if (consulta.getClinica() != null && status.equalsIgnoreCase(consulta.getStatus_consulta())) {
                resultado.add(consulta);
            }
        }
        return resultado;
    }

    public List<Consulta> getAllConsultasPorPeriodo (LocalDate inicio, LocalDate fim) {
        List<Consulta> resultado = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (Consulta consulta : consultas) {
            if (consulta.getData_consulta() != null) {
                try {
                    LocalDate dataConsulta = LocalDate.parse(consulta.getData_consulta(), formatter);

                    if (dataConsulta.isEqual(inicio) || dataConsulta.isEqual(fim) && (dataConsulta.isEqual(fim) || dataConsulta.isBefore(fim))) {
                       resultado.add(consulta);
                    }
                } catch (Exception e) {
                    System.out.println("Data inválida na consulta: " + consulta.getId());
                }
            }
        }
        return resultado;
    }

    public List<Consulta> getAllConsultasPorMedicoEPeriodo (long idMedico,  LocalDate inicio, LocalDate fim) {
        List<Consulta> resultado = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (Consulta consulta : consultas) {
            if (consulta.getMedico() != null && idMedico == consulta.getMedico().getId_medico()) {
                try {
                    LocalDate dataConsulta = LocalDate.parse(consulta.getData_consulta(), formatter);

                    if (dataConsulta.isEqual(inicio) || dataConsulta.isEqual(fim) && (dataConsulta.isEqual(fim) || dataConsulta.isBefore(fim))) {
                        resultado.add(consulta);
                    }
                } catch (Exception e) {
                    System.out.println("Data inválida na consulta: " + consulta.getId());
                }
            }
        }
        return resultado;
    }

    public List<Consulta> getAllConsultasPorClinicaEPeriodo (long idClinica,  LocalDate inicio, LocalDate fim) {
        List<Consulta> resultado = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (Consulta consulta : consultas) {
            if (consulta.getClinica() != null && idClinica == consulta.getClinica().getId_clinica()) {
                try {
                    LocalDate dataConsulta = LocalDate.parse(consulta.getData_consulta(), formatter);

                    if (dataConsulta.isEqual(inicio) || dataConsulta.isEqual(fim) && (dataConsulta.isEqual(fim) || dataConsulta.isBefore(fim))) {
                        resultado.add(consulta);
                    }
                } catch (Exception e) {
                    System.out.println("Data inválida na consulta: " + consulta.getId());
                }
            }
        }
        return resultado;
    }


}

