package br.edu.ifsp.clinica_api.model;

import lombok.Data;

@Data
public class Clinica {
    private long id_clinica;
    private String email;
    private String celular;
    private String telefone;
    private Endereco endereco;
}