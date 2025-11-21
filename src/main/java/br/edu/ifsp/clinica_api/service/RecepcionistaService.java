package br.edu.ifsp.clinica_api.service;

import br.edu.ifsp.clinica_api.dto.RecepcionistaDTO;
import br.edu.ifsp.clinica_api.model.Recepcionista;
import br.edu.ifsp.clinica_api.model.enums.Papel;
import br.edu.ifsp.clinica_api.repository.RecepcionistaRepository;
import br.edu.ifsp.clinica_api.repository.UnidadeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecepcionistaService {
    private final RecepcionistaRepository recepcionistaRepository;
    private final EnderecoService enderecoService;
    private final UnidadeRepository unidadeRepository;
    private final UsuarioService usuarioService;

    public List<Recepcionista> listarTodos() {
        return recepcionistaRepository.findAll();
    }

    public Recepcionista cadastrar(RecepcionistaDTO dto) {
        Recepcionista recepcionista = new Recepcionista();

        recepcionista.setNome(dto.getNome());
        recepcionista.setEmail(dto.getEmail());
        recepcionista.setCelular(dto.getCelular());
        recepcionista.setCpf(dto.getCpf());
        recepcionista.setDataNascimento(dto.getDataNascimento());
        recepcionista.setDataContratacao(dto.getDataContratacao());
        recepcionista.setSexo(dto.getSexo());
        recepcionista.setGenero(dto.getGenero());
        recepcionista.setEstadoCivil(dto.getEstadoCivil());

        // Unidade
        recepcionista.setUnidade(
                unidadeRepository.findById(dto.getUnidadeId())
                        .orElseThrow(() -> new RuntimeException("Unidade não encontrada"))
        );

        // Endereço
        recepcionista.setEndereco(enderecoService.getOrCreate(dto.getEndereco()));

        recepcionista = recepcionistaRepository.save(recepcionista);

        usuarioService.criarUsuario(
                dto.getEmail(),
                dto.getSenha(),
                recepcionista.getId(),
                Papel.RECEPCIONISTA
        );

        return recepcionista;
    }

    public void excluir(Long id) {
        recepcionistaRepository.deleteById(id);
    }
}
