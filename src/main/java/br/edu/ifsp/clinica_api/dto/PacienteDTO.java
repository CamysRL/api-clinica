package br.edu.ifsp.clinica_api.dto;

import br.edu.ifsp.clinica_api.model.enums.EstadoCivil;
import br.edu.ifsp.clinica_api.model.enums.Genero;
import br.edu.ifsp.clinica_api.model.enums.Sexo;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PacienteDTO {
    private String nome;
    private String email;
    private String celular;
    private String cpf;
    private LocalDate dataNascimento;
    private Sexo sexo;
    private Genero genero;
    private EstadoCivil estadoCivil;

    private EnderecoDTO endereco;
}
