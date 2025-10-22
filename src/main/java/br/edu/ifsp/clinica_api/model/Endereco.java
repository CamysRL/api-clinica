package br.edu.ifsp.clinica_api.model;

import lombok.Data;

@Data
public class Endereco {
    private String logradouro;
    private int numero;
    private String cidade;
    private String estado;
    private String cep;

}
