package br.edu.ifsp.clinica_api.dto;

import br.edu.ifsp.clinica_api.model.enums.EstadoCivil;
import br.edu.ifsp.clinica_api.model.enums.Genero;
import br.edu.ifsp.clinica_api.model.enums.Sexo;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MedicoDTO {

    private String nome;
    private String email;
    private String celular;
    private String cpf;
    private String crm;
    private String rqe;
    private LocalDate dataNascimento;
    private LocalDate dataContratacao;
    private Sexo sexo;
    private Genero genero;
    private EstadoCivil estadoCivil;

    private Long unidadeId;
    private Long especialidadeId;

    private EnderecoDTO endereco;

    private String senha;
}

