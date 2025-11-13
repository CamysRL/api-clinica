package br.edu.ifsp.clinica_api.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "paciente")
@Data
public class Unidade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String celular;

    @Column(nullable = false, unique = true)
    private String telefone;

    @ManyToOne
    @JoinColumn(name = "id_endereco", nullable = false)
    private Endereco endereco;
}