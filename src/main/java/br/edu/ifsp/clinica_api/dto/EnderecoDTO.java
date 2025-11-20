package br.edu.ifsp.clinica_api.dto;

import lombok.Data;

@Data
public class EnderecoDTO {
    private Long id;
    private String logradouro;
    private Integer numero;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;
    private String complemento;
}
