package br.edu.ifsp.clinica_api.service;

import br.edu.ifsp.clinica_api.model.Unidade;
import br.edu.ifsp.clinica_api.repository.UnidadeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UnidadeService {
    private final UnidadeRepository unidadeRepository;

    public UnidadeService(UnidadeRepository unidadeRepository) {
        this.unidadeRepository = unidadeRepository;
    }

    public List<Unidade> listarTodos() {
        return unidadeRepository.findAll();
    }

    public Unidade cadastrar(Unidade unidade) {
        return unidadeRepository.save(unidade);
    }

    public void excluir(Long id) {
        unidadeRepository.deleteById(id);
    }
}
