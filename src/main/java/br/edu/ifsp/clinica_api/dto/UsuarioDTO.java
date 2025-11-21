package br.edu.ifsp.clinica_api.dto;

import br.edu.ifsp.clinica_api.model.enums.Papel;
import lombok.Data;

@Data
public class UsuarioDTO {
    private String email;
    private String senha;
    private Papel papel;
    private Long idReferencia;
}
