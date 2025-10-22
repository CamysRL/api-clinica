package br.edu.ifsp.clinica_api.model;

import lombok.Data;

@Data
public class Consulta {
    private long id;
    private String data_consulta;
    private String data_marcada;
    private String hora_consulta;
    private String hora_marcada;
    private String status_consulta;
    private Paciente paciente;
    private Medico medico;
    private Clinica clinica;
}
