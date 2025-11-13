package br.edu.ifsp.clinica_api.repository;

import br.edu.ifsp.clinica_api.model.Unidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnidadeRepository extends JpaRepository<Unidade, Long> {
}
