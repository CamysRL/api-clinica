package br.edu.ifsp.clinica_api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ConsultaNotFoundException extends RuntimeException {
    public ConsultaNotFoundException(Long id) {
        super("Consulta ot found with ID: " + id);
    }
}
