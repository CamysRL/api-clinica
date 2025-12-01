package br.edu.ifsp.clinica_api.repository;

import br.edu.ifsp.clinica_api.model.Consulta;
import br.edu.ifsp.clinica_api.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long>  {
    List<Consulta> findByMedicoId(long idMedico);

    List<Consulta> findByPacienteId(long idPaciente);

    List<Consulta> findByUnidadeId(long idUnidade);

    List<Consulta> findByStatus(String status);

    List<Consulta> findByDataConsultaBetween(LocalDate inicio, LocalDate fim);

    List<Consulta> findByMedico_IdAndDataConsultaBetween(Long idMedico, LocalDate inicio, LocalDate fim);

    List<Consulta> findByUnidade_IdAndDataConsultaBetween(Long idUnidade, LocalDate inicio, LocalDate fim);

    List<Consulta> findByPaciente_IdAndDataConsultaBetween(Long idPaciente, LocalDate inicio, LocalDate fim);

}
