package br.edu.ifsp.clinica_api.controller;


import br.edu.ifsp.clinica_api.model.Consulta;
import br.edu.ifsp.clinica_api.service.ConsultaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/consultas")
public class MedicoController {
    private final ConsultaService consultaService;

    public MedicoController(ConsultaService consultaService) {
        this.consultaService = consultaService;
    }

    @GetMapping("/medico/{id_medico}")
    public ResponseEntity<List<Consulta>> getAllConsultasPorMedico(@PathVariable long id_medico) {

        List<Consulta> consultas = consultaService.getAllConsultasPorMedico(id_medico);
        return ResponseEntity.ok(consultas);
    }

    @GetMapping("/medico/{id_medico}/periodo")
    public ResponseEntity<List<Consulta>> getAllConsultasPorMedicoEPeriodo(@PathVariable long id_medico,
                                                                           @RequestParam String inicio, @RequestParam String fim) {

        LocalDate dataInicio = LocalDate.parse(inicio);
        LocalDate dataFim = LocalDate.parse(fim);

        List<Consulta> consultas = consultaService.getAllConsultasPorMedicoEPeriodo(id_medico, dataInicio, dataFim);
        return ResponseEntity.ok(consultas);
    }

}
