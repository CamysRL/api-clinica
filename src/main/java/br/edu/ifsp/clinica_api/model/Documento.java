package br.edu.ifsp.clinica_api.model;

import lombok.Data;

@Data
public class Documento {
    private Consulta consulta;
    private String arquivo_url;
    private String data_criacao;
}