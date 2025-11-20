package br.edu.ifsp.clinica_api.repository;


import br.edu.ifsp.clinica_api.model.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Long> {

    Optional<Endereco> findByLogradouroAndNumeroAndCidadeAndEstadoAndCep(
            String logradouro,
            Integer numero,
            String cidade,
            String estado,
            String cep
    );
}
