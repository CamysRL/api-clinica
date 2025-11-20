package br.edu.ifsp.clinica_api.service;

import br.edu.ifsp.clinica_api.model.Medico;
import br.edu.ifsp.clinica_api.repository.MedicoRepository;

import java.util.List;

public class MedicoService {
    private final MedicoRepository medicoRepository;

    public MedicoService(MedicoRepository medicoRepository) {
        this.medicoRepository = medicoRepository;
    }

    public List<Medico> listarTodos() {
        return medicoRepository.findAll();
    }

    public Medico salvar(Medico medico) {
        return medicoRepository.save(medico);
    }

    public void excluir(Long id) {
        medicoRepository.deleteById(id);
    }
}
