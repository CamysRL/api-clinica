package br.edu.ifsp.clinica_api.dto;

import br.edu.ifsp.clinica_api.model.enums.Papel;
import lombok.Data;
import java.time.LocalDate;

@Data
public class UsuarioResponseDTO {

    private Long id;
    private String email;
    private boolean ativo;
    private LocalDate dataCriacao;
    private Papel papel;
    private Long idReferencia;
}
