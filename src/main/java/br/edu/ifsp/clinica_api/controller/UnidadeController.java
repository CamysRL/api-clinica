package br.edu.ifsp.clinica_api.controller;

import br.edu.ifsp.clinica_api.model.Unidade;
import br.edu.ifsp.clinica_api.service.UnidadeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/unidades")
public class UnidadeController {
    private final UnidadeService unidadeService;

    public UnidadeController(UnidadeService unidadeService) {
        this.unidadeService = unidadeService;
    }

    @GetMapping
    public List<Unidade> listar() {
        return unidadeService.listarTodos();
    }

    @PostMapping
    public Unidade criar(@RequestBody Unidade unidade) {
        return unidadeService.cadastrar(unidade);
    }

    @DeleteMapping("/{id}")
    public void excluir(@PathVariable Long id) {
        unidadeService.excluir(id);
    }
}
