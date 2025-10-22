package br.edu.ifsp.clinica_api.model;

import lombok.Data;

@Data
public class Medico {
    private long id_medico;
    private String nome;
    private String email;
    private String celular;
    private String cpf;
    private String especialidade;
    private String crm;
    private String rqe;
    private String data_contratacao;
    private Endereco endereco;
}