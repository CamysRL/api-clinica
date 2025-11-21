package br.edu.ifsp.clinica_api.service;

import br.edu.ifsp.clinica_api.dto.MedicoDTO;
import br.edu.ifsp.clinica_api.model.Medico;
import br.edu.ifsp.clinica_api.model.enums.Papel;
import br.edu.ifsp.clinica_api.repository.EspecialidadeRepository;
import br.edu.ifsp.clinica_api.repository.MedicoRepository;
import br.edu.ifsp.clinica_api.repository.UnidadeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicoService {
    private final MedicoRepository medicoRepository;
    private final EnderecoService enderecoService;
    private final UnidadeRepository unidadeRepository;
    private final UsuarioService usuarioService;
    private final EspecialidadeRepository especialidadeRepository;

    public List<Medico> listarTodos() {
        return medicoRepository.findAll();
    }

    public Medico cadastrar(MedicoDTO dto) {
        Medico medico = new Medico();

        medico.setNome(dto.getNome());
        medico.setEmail(dto.getEmail());
        medico.setCelular(dto.getCelular());
        medico.setCpf(dto.getCpf());
        medico.setCrm(dto.getCrm());
        medico.setRqe(dto.getRqe());
        medico.setDataNascimento(dto.getDataNascimento());
        medico.setDataContratacao(dto.getDataContratacao());
        medico.setSexo(dto.getSexo());
        medico.setGenero(dto.getGenero());
        medico.setEstadoCivil(dto.getEstadoCivil());

        // Unidade
        medico.setUnidade(
                unidadeRepository.findById(dto.getUnidadeId())
                        .orElseThrow(() -> new RuntimeException("Unidade não encontrada"))
        );

        // Especialidade
        medico.setEspecialidade(
                especialidadeRepository.findById(dto.getEspecialidadeId())
                        .orElseThrow(() -> new RuntimeException("Especialidade não encontrada"))
        );

        medico.setEndereco(enderecoService.getOrCreate(dto.getEndereco()));

        medico = medicoRepository.save(medico);

        usuarioService.criarUsuario(
                dto.getEmail(),
                dto.getSenha(),
                medico.getId(),
                Papel.MEDICO
        );

        return medico;

    }

    public void excluir(Long id) {
        medicoRepository.deleteById(id);
    }
}
