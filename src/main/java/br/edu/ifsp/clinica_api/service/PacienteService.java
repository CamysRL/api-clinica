package br.edu.ifsp.clinica_api.service;

import br.edu.ifsp.clinica_api.dto.PacienteDTO;
import br.edu.ifsp.clinica_api.model.Paciente;
import br.edu.ifsp.clinica_api.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PacienteService {
    private final PacienteRepository pacienteRepository;
    private final EnderecoService enderecoService;

    public List<Paciente> listarTodos() {
        return pacienteRepository.findAll();
    }

    public Paciente cadastrar(PacienteDTO dto) {
        Paciente paciente = new Paciente();

        paciente.setNome(dto.getNome());
        paciente.setEmail(dto.getEmail());
        paciente.setCelular(dto.getCelular());
        paciente.setCpf(dto.getCpf());
        paciente.setDataNascimento(dto.getDataNascimento());
        paciente.setSexo(dto.getSexo());
        paciente.setGenero(dto.getGenero());
        paciente.setEstadoCivil(dto.getEstadoCivil());

        // Endereço
        paciente.setEndereco(enderecoService.getOrCreate(dto.getEndereco()));

        return pacienteRepository.save(paciente);
    }

    public void excluir(Long id) {
        pacienteRepository.deleteById(id);
    }
}
