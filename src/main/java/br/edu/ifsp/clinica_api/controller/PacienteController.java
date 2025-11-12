package br.edu.ifsp.clinica_api.controller;

import br.edu.ifsp.clinica_api.service.ConsultaService;

import br.edu.ifsp.clinica_api.model.Consulta;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/consultas")

public class PacienteController {

    private final ConsultaService consultaService;

    public PacienteController(ConsultaService consultaService) {
        this.consultaService = consultaService;
    }

    @GetMapping("/paciente/{id_paciente}")
    public ResponseEntity<List<Consulta>> getAllConsultasPorPaciente(@PathVariable long id_paciente) {

        List<Consulta> consultas = consultaService.getAllConsultasPorPaciente(id_paciente);
        return ResponseEntity.ok(consultas);
    }

    @GetMapping("/paciente/{id_paciente}/periodo")
    public ResponseEntity<List<Consulta>> getAllConsultasPorPacienteEPeriodo(@PathVariable long id_paciente,
                                                                            @RequestParam String inicio, @RequestParam String fim) {

        LocalDate dataInicio = LocalDate.parse(inicio);
        LocalDate dataFim = LocalDate.parse(fim);

        List<Consulta> consultas = consultaService.getAllConsultasPorPacienteEPeriodo(id_paciente, dataInicio, dataFim);
        return ResponseEntity.ok(consultas);
    }
}
