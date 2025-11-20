package br.edu.ifsp.clinica_api.controller;

import br.edu.ifsp.clinica_api.dto.PacienteDTO;
import br.edu.ifsp.clinica_api.model.Paciente;

import br.edu.ifsp.clinica_api.service.PacienteService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {
    private final PacienteService pacienteService;

    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    @GetMapping
    public List<Paciente> listar() {
        return pacienteService.listarTodos();
    }

    @PostMapping
    public Paciente criar(@RequestBody PacienteDTO paciente) {
        return pacienteService.cadastrar(paciente);
    }

    @DeleteMapping("/{id}")
    public void excluir(@PathVariable Long id) {
        pacienteService.excluir(id);
    }
}
