package br.edu.ifsp.clinica_api.service;

import br.edu.ifsp.clinica_api.model.Paciente;
import br.edu.ifsp.clinica_api.repository.PacienteRepository;

import java.util.List;

public class PacienteService {
    private final PacienteRepository pacienteRepository;

    public PacienteService(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    public List<Paciente> listarTodos() {
        return pacienteRepository.findAll();
    }

    public Paciente salvar(Paciente paciente) {
        return pacienteRepository.save(paciente);
    }

    public void excluir(Long id) {
        pacienteRepository.deleteById(id);
    }
}
