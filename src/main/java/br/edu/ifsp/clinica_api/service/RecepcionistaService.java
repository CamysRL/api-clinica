package br.edu.ifsp.clinica_api.service;

import br.edu.ifsp.clinica_api.model.Recepcionista;
import br.edu.ifsp.clinica_api.repository.RecepcionistaRepository;

import java.util.List;

public class RecepcionistaService {
    private final RecepcionistaRepository recepcionistaRepository;

    public RecepcionistaService(RecepcionistaRepository recepcionistaRepository) {
        this.recepcionistaRepository = recepcionistaRepository;
    }

    public List<Recepcionista> listarTodos() {
        return recepcionistaRepository.findAll();
    }

    public Recepcionista salvar(Recepcionista recepcionista) {
        return recepcionistaRepository.save(recepcionista);
    }

    public void excluir(Long id) {
        recepcionistaRepository.deleteById(id);
    }
}
