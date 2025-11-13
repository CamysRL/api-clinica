package br.edu.ifsp.clinica_api.model;

import br.edu.ifsp.clinica_api.model.enums.TipoDocumento;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "documento")
@Data
public class Documento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "arquivo_url",nullable = false)
    private String arquivoUrl;

    @Column(name = "data_emissao", nullable = false)
    private LocalDate dataEmissao;

    @ManyToOne
    @JoinColumn(name = "id_consulta", nullable = false)
    private Consulta consulta;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_documento", nullable = false)
    private TipoDocumento tipoDocumento;
}