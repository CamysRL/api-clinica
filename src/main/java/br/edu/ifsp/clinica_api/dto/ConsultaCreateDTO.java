package br.edu.ifsp.clinica_api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ConsultaCreateDTO {

    @NotNull(message = "O status da consulta é obrigatório")
    private String status;

    @NotNull(message = "A data da consulta é obrigatória")
    private String dataConsulta;

    @NotNull(message = "A hora da consulta é obrigatória")
    private String horaConsulta;

    @NotNull(message = "O ID do paciente é obrigatório")
    private Long pacienteId;

    @NotNull(message = "O ID do médico é obrigatório")
    private Long medicoId;

    @NotNull(message = "O ID da unidade é obrigatório")
    private Long unidadeId;
}
