package br.edu.ifsp.clinica_api.model;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "especialidade")
@Data
public class Especialidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id_especialidade;

    @Column(nullable = false)
    private String descricao;
}
