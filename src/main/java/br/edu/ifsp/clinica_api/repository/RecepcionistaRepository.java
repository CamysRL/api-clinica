package br.edu.ifsp.clinica_api.repository;

import br.edu.ifsp.clinica_api.model.Recepcionista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecepcionistaRepository extends JpaRepository<Recepcionista, Long> {
}
