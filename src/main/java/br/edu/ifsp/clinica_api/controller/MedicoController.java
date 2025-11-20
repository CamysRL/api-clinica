package br.edu.ifsp.clinica_api.controller;

import br.edu.ifsp.clinica_api.dto.MedicoDTO;
import br.edu.ifsp.clinica_api.model.Medico;
import br.edu.ifsp.clinica_api.service.MedicoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medicos")
public class MedicoController {
    private final MedicoService medicoService;

    public MedicoController(MedicoService medicoService) {
        this.medicoService = medicoService;
    }

    @GetMapping
    public List<Medico> listar() {
        return medicoService.listarTodos();
    }

    @PostMapping
    public Medico criar(@RequestBody MedicoDTO medicoDTO) {
        return medicoService.cadastrar(medicoDTO);
    }

    @DeleteMapping("/{id}")
    public void excluir(@PathVariable Long id) {
        medicoService.excluir(id);
    }
}
