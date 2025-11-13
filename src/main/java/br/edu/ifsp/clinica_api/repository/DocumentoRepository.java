package br.edu.ifsp.clinica_api.repository;

import br.edu.ifsp.clinica_api.model.Documento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentoRepository extends JpaRepository<Documento, Long>  {
}
