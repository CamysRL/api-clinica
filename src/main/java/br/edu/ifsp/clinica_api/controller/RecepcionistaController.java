package br.edu.ifsp.clinica_api.controller;

import br.edu.ifsp.clinica_api.model.Recepcionista;
import br.edu.ifsp.clinica_api.service.RecepcionistaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public class RecepcionistaController {
    private final RecepcionistaService recepcionistaService;

    public RecepcionistaController(RecepcionistaService recepcionistaService) {
        this.recepcionistaService = recepcionistaService;
    }

    @GetMapping
    public List<Recepcionista> listar() {
        return recepcionistaService.listarTodos();
    }

    @PostMapping
    public Recepcionista criar(@RequestBody Recepcionista recepcionista) {
        return recepcionistaService.salvar(recepcionista);
    }

    @DeleteMapping("/{id}")
    public void excluir(@PathVariable Long id) {
        recepcionistaService.excluir(id);
    }
}
