package br.edu.ifsp.clinica_api.service;

import br.edu.ifsp.clinica_api.exceptions.ConsultaNotFoundException;
import br.edu.ifsp.clinica_api.model.Consulta;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class ConsultaService {

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

    public Consulta updateConsulta(long id, Consulta updatedConsulta) {
        Consulta consulta = getConsultaById(id);
        updatedConsulta.setId(id);
        consultas.remove(consulta);
        consultas.add(updatedConsulta);
        return updatedConsulta;
    }

    public Consulta updateStatus(long id, String status) {
        List<String> validos = List.of("PENDENTE", "CANCELADA", "REALIZADA", "REMARCADA");
        if (!validos.contains(status.toUpperCase())) {
            throw new IllegalArgumentException(
                    "Status informado é inválido: '" + status + "'. Valores permitidos: PENDENTE, CANCELADA, REALIZADA, REMARCADA."
            );

        }

        Consulta consulta = getConsultaById(id);
        consulta.setStatus_consulta(status.toUpperCase());
        return consulta;
    }

    public List<Consulta> getAllConsultasPorMedico(long idMedico) {
        List<Consulta> resultado = new ArrayList<>();
        for (Consulta consulta : consultas) {
            if (consulta.getMedico() != null &&
                    idMedico == consulta.getMedico().getId_medico()) {
                resultado.add(consulta);
            }
        }
        return resultado;
    }

    public List<Consulta> getAllConsultasPorPaciente(long idPaciente) {
        List<Consulta> resultado = new ArrayList<>();
        for (Consulta consulta : consultas) {
            if (consulta.getPaciente() != null &&
                    idPaciente == consulta.getPaciente().getId_paciente()) {
                resultado.add(consulta);
            }
        }
        return resultado;
    }

    public List<Consulta> getAllConsultasPorClinica(long idClinica) {
        List<Consulta> resultado = new ArrayList<>();
        for (Consulta consulta : consultas) {
            if (consulta.getUnidade() != null &&
                    idClinica == consulta.getUnidade().getId_clinica()) {
                resultado.add(consulta);
            }
        }
        return resultado;
    }

    public List<Consulta> getAllConsultasPorStatus(String status) {
        List<Consulta> resultado = new ArrayList<>();
        for (Consulta consulta : consultas) {
            if (status.equalsIgnoreCase(consulta.getStatus_consulta())) {
                resultado.add(consulta);
            }
        }
        return resultado;
    }

    public List<Consulta> getAllConsultasPorPeriodo(LocalDate inicio, LocalDate fim) {
        List<Consulta> resultado = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (Consulta consulta : consultas) {
            if (consulta.getData_consulta() != null) {
                try {
                    LocalDate dataConsulta = LocalDate.parse(consulta.getData_consulta(), formatter);

                    if ((dataConsulta.isEqual(inicio) || dataConsulta.isAfter(inicio)) &&
                            (dataConsulta.isEqual(fim) || dataConsulta.isBefore(fim))) {
                        resultado.add(consulta);
                    }
                } catch (Exception e) {
                    throw new IllegalArgumentException("Formato de data inválido na consulta ID "
                            + consulta.getId() + ". Use o formato 'yyyy-MM-dd'.");

                }
            }
        }
        return resultado;
    }

    public List<Consulta> getAllConsultasPorMedicoEPeriodo(long idMedico, LocalDate inicio, LocalDate fim) {
        List<Consulta> resultado = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (Consulta consulta : consultas) {
            if (consulta.getMedico() != null &&
                    idMedico == consulta.getMedico().getId_medico() &&
                    consulta.getData_consulta() != null) {
                try {
                    LocalDate dataConsulta = LocalDate.parse(consulta.getData_consulta(), formatter);

                    if ((dataConsulta.isEqual(inicio) || dataConsulta.isAfter(inicio)) &&
                            (dataConsulta.isEqual(fim) || dataConsulta.isBefore(fim))) {
                        resultado.add(consulta);
                    }
                } catch (Exception e) {
                    throw new IllegalArgumentException("Data inválida na consulta ID " + consulta.getId());
                }
            }
        }
        return resultado;
    }

    public List<Consulta> getAllConsultasPorClinicaEPeriodo(long idClinica, LocalDate inicio, LocalDate fim) {
        List<Consulta> resultado = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (Consulta consulta : consultas) {
            if (consulta.getUnidade() != null &&
                    idClinica == consulta.getUnidade().getId_clinica() &&
                    consulta.getData_consulta() != null) {
                try {
                    LocalDate dataConsulta = LocalDate.parse(consulta.getData_consulta(), formatter);

                    if ((dataConsulta.isEqual(inicio) || dataConsulta.isAfter(inicio)) &&
                            (dataConsulta.isEqual(fim) || dataConsulta.isBefore(fim))) {
                        resultado.add(consulta);
                    }
                } catch (Exception e) {
                    throw new IllegalArgumentException("Data inválida na consulta ID " + consulta.getId());
                }
            }
        }
        return resultado;
    }

    public List<Consulta> getAllConsultasPorPacienteEPeriodo(long idPaciente, LocalDate inicio, LocalDate fim) {
        List<Consulta> resultado = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (Consulta consulta : consultas) {
            if (consulta.getPaciente() != null &&
                    idPaciente == consulta.getPaciente().getId_paciente() &&
                    consulta.getData_consulta() != null) {
                try {
                    LocalDate dataConsulta = LocalDate.parse(consulta.getData_consulta(), formatter);

                    if ((dataConsulta.isEqual(inicio) || dataConsulta.isAfter(inicio)) &&
                            (dataConsulta.isEqual(fim) || dataConsulta.isBefore(fim))) {
                        resultado.add(consulta);
                    }
                } catch (Exception e) {
                    throw new IllegalArgumentException("Data inválida na consulta ID " + consulta.getId());
                }
            }
        }
        return resultado;
    }

}

