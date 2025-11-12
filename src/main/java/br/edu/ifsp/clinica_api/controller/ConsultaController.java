package br.edu.ifsp.clinica_api.controller;

import br.edu.ifsp.clinica_api.model.Consulta;
import br.edu.ifsp.clinica_api.service.ConsultaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/consultas")
public class ConsultaController {

    private final ConsultaService consultaService;

    public ConsultaController(ConsultaService consultaService) {
        this.consultaService = consultaService;
    }

    @PostMapping
    public ResponseEntity<Consulta> createConsulta(@RequestBody Consulta newConsulta) {
        return ResponseEntity.status(HttpStatus.CREATED).body(consultaService.createConsulta(newConsulta));
    }

    @GetMapping
    public ResponseEntity<List<Consulta>> getAllConsultas() {
        return ResponseEntity.ok(consultaService.getAllConsultas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Consulta> getConsultaById(@PathVariable long id) {
        return ResponseEntity.ok(consultaService.getConsultaById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConsulta(@PathVariable long id) {
        consultaService.deleteConsulta(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping()
    public ResponseEntity<Void> deleteAllConsultas() {
        consultaService.deleteAllConsultas();
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Consulta> updateConsulta(@PathVariable long id,
                                                   @RequestBody Consulta updatedConsulta) {
        return ResponseEntity.ok(consultaService.updateConsulta(id, updatedConsulta));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Consulta> updateStatus(@PathVariable long id,
                                                 @RequestParam String status) {
        return ResponseEntity.ok(consultaService.updateStatus(id, status));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Consulta>> getAllConsultasPorStatus(@PathVariable String status) {

        List<Consulta> consultas = consultaService.getAllConsultasPorStatus(status);
        return ResponseEntity.ok(consultas);
    }

    @GetMapping("/periodo")
    public ResponseEntity<List<Consulta>> getAllConsultasPorPeriodo(@RequestParam String inicio, @RequestParam String fim) {

        LocalDate dataInicio = LocalDate.parse(inicio);
        LocalDate dataFim = LocalDate.parse(fim);
        List<Consulta> consultas = consultaService.getAllConsultasPorPeriodo(dataInicio, dataFim);
        return ResponseEntity.ok(consultas);
    }
}