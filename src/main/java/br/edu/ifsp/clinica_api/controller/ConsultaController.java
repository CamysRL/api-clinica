package br.edu.ifsp.clinica_api.controller;

import br.edu.ifsp.clinica_api.dto.ConsultaCreateDTO;
import br.edu.ifsp.clinica_api.model.Consulta;
import br.edu.ifsp.clinica_api.service.ConsultaService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
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

    // ---------------------- CRUD ----------------------

    @PostMapping
    public ResponseEntity<Consulta> createConsulta(@Valid @RequestBody ConsultaCreateDTO dto) {
        Consulta saved = consultaService.createConsulta(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<Consulta>> getAllConsultas() {
        return ResponseEntity.ok(consultaService.getAllConsultas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Consulta> getConsultaById(@PathVariable long id) {
        return ResponseEntity.ok(consultaService.getConsultaById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Consulta> updateConsulta(@PathVariable long id,
                                                   @RequestBody Consulta updatedConsulta) {
        return ResponseEntity.ok(consultaService.updateConsulta(id, updatedConsulta));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConsulta(@PathVariable long id) {
        consultaService.deleteConsulta(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllConsultas() {
        consultaService.deleteAllConsultas();
        return ResponseEntity.noContent().build();
    }

    // ---------------------- STATUS ----------------------

    @PatchMapping("/{id}/status")
    public ResponseEntity<Consulta> updateStatus(@PathVariable long id,
                                                 @RequestParam String status) {
        return ResponseEntity.ok(consultaService.updateStatus(id, status));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Consulta>> getAllConsultasPorStatus(@PathVariable String status) {
        return ResponseEntity.ok(consultaService.getAllConsultasPorStatus(status));
    }

    // ---------------------- FILTROS POR DATA ----------------------

    @GetMapping("/periodo")
    public ResponseEntity<List<Consulta>> getAllConsultasPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim
    ) {
        return ResponseEntity.ok(consultaService.getAllConsultasPorPeriodo(inicio, fim));
    }

    // ---------------------- POR UNIDADE ----------------------

    @GetMapping("/unidade/{idUnidade}")
    public ResponseEntity<List<Consulta>> getAllConsultasPorUnidade(@PathVariable long idUnidade) {
        return ResponseEntity.ok(consultaService.getAllConsultasPorUnidade(idUnidade));
    }

    @GetMapping("/unidade/{idUnidade}/periodo")
    public ResponseEntity<List<Consulta>> getAllConsultasPorUnidadeEPeriodo(
            @PathVariable long idUnidade,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim
    ) {
        return ResponseEntity.ok(consultaService.getAllConsultasPorUnidadeEPeriodo(idUnidade, inicio, fim));
    }

    // ---------------------- POR MÉDICO ----------------------

    @GetMapping("/medico/{idMedico}")
    public ResponseEntity<List<Consulta>> getAllConsultasPorMedico(@PathVariable long idMedico) {
        return ResponseEntity.ok(consultaService.getAllConsultasPorMedico(idMedico));
    }

    @GetMapping("/medico/{idMedico}/periodo")
    public ResponseEntity<List<Consulta>> getAllConsultasPorMedicoEPeriodo(
            @PathVariable long idMedico,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim
    ) {
        return ResponseEntity.ok(consultaService.getAllConsultasPorMedicoEPeriodo(idMedico, inicio, fim));
    }

    // ---------------------- POR PACIENTE ----------------------

    @GetMapping("/paciente/{idPaciente}")
    public ResponseEntity<List<Consulta>> getAllConsultasPorPaciente(@PathVariable long idPaciente) {
        return ResponseEntity.ok(consultaService.getAllConsultasPorPaciente(idPaciente));
    }

    @GetMapping("/paciente/{idPaciente}/periodo")
    public ResponseEntity<List<Consulta>> getAllConsultasPorPacienteEPeriodo(
            @PathVariable long idPaciente,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim
    ) {
        return ResponseEntity.ok(consultaService.getAllConsultasPorPacienteEPeriodo(idPaciente, inicio, fim));
    }
}
