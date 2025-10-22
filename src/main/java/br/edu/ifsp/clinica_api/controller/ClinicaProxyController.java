package br.edu.ifsp.clinica_api.controller;

import br.edu.ifsp.clinica_api.model.Consulta;
import br.edu.ifsp.clinica_api.service.ClinicaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/clinica")
public class ClinicaProxyController {

    private final ClinicaService clinicaService;
    private static final String STATIC_AUTH_TOKEN = "token-secreto";

    public ClinicaProxyController(ClinicaService clinicaService) {
        this.clinicaService = clinicaService;
    }

    private boolean isAuthorized(String token) {
        return STATIC_AUTH_TOKEN.equals(token);
    }

    @PostMapping
    public ResponseEntity<Consulta> createConsulta(@RequestBody Consulta newConsulta,
                                                   @RequestHeader("X-Auth-Token") String token) {
        if (!isAuthorized(token)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(clinicaService.createConsulta(newConsulta));
    }

    @GetMapping
    public ResponseEntity<List<Consulta>> getAllConsultas(@RequestHeader("X-Auth-Token") String token) {
        if (!isAuthorized(token)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        return ResponseEntity.ok(clinicaService.getAllConsultas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Consulta> getConsultaById(@PathVariable long id,
                                                    @RequestHeader("X-Auth-Token") String token) {
        if (!isAuthorized(token)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        return ResponseEntity.ok(clinicaService.getConsultaById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConsulta(@PathVariable long id,
                                               @RequestHeader("X-Auth-Token") String token) {
        if (!isAuthorized(token)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        clinicaService.deleteConsulta(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping()
    public ResponseEntity<Void> deleteAllConsultas(@RequestHeader("X-Auth-Token") String token) {
        if (!isAuthorized(token)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        clinicaService.deleteAllConsultas();
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Consulta> updateConsulta(@PathVariable long id,
                                                   @RequestBody Consulta updatedConsulta,
                                                   @RequestHeader("X-Auth-Token") String token) {
        if (!isAuthorized(token)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        return ResponseEntity.ok(clinicaService.updateConsulta(id, updatedConsulta));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Consulta> updateStatus(@PathVariable long id,
                                                 @RequestParam String status,
                                                 @RequestHeader("X-Auth-Token") String token) {
        if (!isAuthorized(token)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        return ResponseEntity.ok(clinicaService.updateStatus(id, status));
    }

    @GetMapping("/medico/{id_medico}")
    public ResponseEntity<List<Consulta>> getAllConsultasPorMedico(
            @PathVariable long id_medico,
            @RequestHeader("X-Auth-Token") String token) {

        if (!isAuthorized(token)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        List<Consulta> consultas = clinicaService.getAllConsultasPorMedico(id_medico);
        return ResponseEntity.ok(consultas);
    }

    @GetMapping("/paciente/{id_paciente}")
    public ResponseEntity<List<Consulta>> getAllConsultasPorPaciente(
            @PathVariable long id_paciente,
            @RequestHeader("X-Auth-Token") String token) {

        if (!isAuthorized(token)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        List<Consulta> consultas = clinicaService.getAllConsultasPorPaciente(id_paciente);
        return ResponseEntity.ok(consultas);
    }

    @GetMapping("/clinica/{id_clinica}")
    public ResponseEntity<List<Consulta>> getAllConsultasPorClinica(
            @PathVariable long id_clinica,
            @RequestHeader("X-Auth-Token") String token) {

        if (!isAuthorized(token)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        List<Consulta> consultas = clinicaService.getAllConsultasPorClinica(id_clinica);
        return ResponseEntity.ok(consultas);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Consulta>> getAllConsultasPorStatus(
            @PathVariable String status,
            @RequestHeader("X-Auth-Token") String token) {

        if (!isAuthorized(token)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        List<Consulta> consultas = clinicaService.getAllConsultasPorStatus(status);
        return ResponseEntity.ok(consultas);
    }

    @GetMapping("/periodo")
    public ResponseEntity<List<Consulta>> getAllConsultasPorPeriodo(
            @RequestParam String inicio,
            @RequestParam String fim,
            @RequestHeader("X-Auth-Token") String token) {

        if (!isAuthorized(token)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        LocalDate dataInicio = LocalDate.parse(inicio);
        LocalDate dataFim = LocalDate.parse(fim);
        List<Consulta> consultas = clinicaService.getAllConsultasPorPeriodo(dataInicio, dataFim);
        return ResponseEntity.ok(consultas);
    }

    @GetMapping("/medico/{id_medico}/periodo")
    public ResponseEntity<List<Consulta>> getAllConsultasPorMedicoEPeriodo(
            @PathVariable long id_medico,
            @RequestParam String inicio,
            @RequestParam String fim,
            @RequestHeader("X-Auth-Token") String token) {

        if (!isAuthorized(token)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        LocalDate dataInicio = LocalDate.parse(inicio);
        LocalDate dataFim = LocalDate.parse(fim);

        List<Consulta> consultas = clinicaService.getAllConsultasPorMedicoEPeriodo(id_medico, dataInicio, dataFim);
        return ResponseEntity.ok(consultas);
    }

    @GetMapping("/{id_clinica}/periodo")
    public ResponseEntity<List<Consulta>> getAllConsultasPorClinicaEPeriodo(
            @PathVariable
            long id_clinica,
            @RequestParam String inicio,
            @RequestParam String fim,
            @RequestHeader("X-Auth-Token") String token) {

        if (!isAuthorized(token)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        LocalDate dataInicio = LocalDate.parse(inicio);
        LocalDate dataFim = LocalDate.parse(fim);

        List<Consulta> consultas = clinicaService.getAllConsultasPorClinicaEPeriodo(id_clinica, dataInicio, dataFim);
        return ResponseEntity.ok(consultas);
    }




}