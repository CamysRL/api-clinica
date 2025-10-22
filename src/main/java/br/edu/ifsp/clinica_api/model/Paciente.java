package br.edu.ifsp.clinica_api.model;

import lombok.Data;

@Data
public class Paciente {
    private long id_paciente;
    private String nome;
    private String email;
    private String celular;
    private String cpf;
    private Endereco endereco;
    private String data_nasc;
}
