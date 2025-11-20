package br.edu.ifsp.clinica_api.service;

import br.edu.ifsp.clinica_api.dto.EnderecoDTO;
import br.edu.ifsp.clinica_api.model.Endereco;
import br.edu.ifsp.clinica_api.repository.EnderecoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EnderecoService {
    private final EnderecoRepository enderecoRepository;

    public Endereco getOrCreate(EnderecoDTO dto) {

        // Caso o cliente envie apenas o ID
        if (dto.getId() != null) {
            return enderecoRepository.findById(dto.getId())
                    .orElseThrow(() -> new RuntimeException("Endereço não encontrado"));
        }

        // Buscar endereço igual no banco
        Optional<Endereco> existente = enderecoRepository
                .findByLogradouroAndNumeroAndCidadeAndEstadoAndCep(
                        dto.getLogradouro(),
                        dto.getNumero(),
                        dto.getCidade(),
                        dto.getEstado(),
                        dto.getCep()
                );

        if (existente.isPresent()) {
            return existente.get();
        }

        // Criar novo
        Endereco novo = new Endereco();
        novo.setLogradouro(dto.getLogradouro());
        novo.setNumero(dto.getNumero());
        novo.setCidade(dto.getCidade());
        novo.setEstado(dto.getEstado());
        novo.setCep(dto.getCep());
        novo.setBairro(dto.getBairro());
        novo.setComplemento(dto.getComplemento());

        return enderecoRepository.save(novo);
    }
}
