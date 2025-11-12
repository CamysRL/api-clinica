package br.edu.ifsp.clinica_api.controller;

import br.edu.ifsp.clinica_api.model.Consulta;
import br.edu.ifsp.clinica_api.service.ConsultaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/consultas")
public class ClinicaController {
    private final ConsultaService consultaService;

    public ClinicaController(ConsultaService consultaService) {
        this.consultaService = consultaService;
    }

    @GetMapping("/clinica/{id_clinica}")
    public ResponseEntity<List<Consulta>> getAllConsultasPorClinica(@PathVariable long id_clinica) {

        List<Consulta> consultas = consultaService.getAllConsultasPorClinica(id_clinica);
        return ResponseEntity.ok(consultas);
    }

    @GetMapping("/clinica/{id_clinica}/periodo")
    public ResponseEntity<List<Consulta>> getAllConsultasPorClinicaEPeriodo(@PathVariable long id_clinica,
                                                                            @RequestParam String inicio, @RequestParam String fim) {

        LocalDate dataInicio = LocalDate.parse(inicio);
        LocalDate dataFim = LocalDate.parse(fim);

        List<Consulta> consultas = consultaService.getAllConsultasPorClinicaEPeriodo(id_clinica, dataInicio, dataFim);
        return ResponseEntity.ok(consultas);
    }

}
