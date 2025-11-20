package br.edu.ifsp.clinica_api.controller;

import br.edu.ifsp.clinica_api.model.Usuario;
import br.edu.ifsp.clinica_api.service.UsuarioService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public class UsuarioController {
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<Usuario> listar() {
        return usuarioService.listarTodos();
    }

    @PostMapping
    public Usuario criar(@RequestBody Usuario usuario) {
        return usuarioService.salvar(usuario);
    }

    @DeleteMapping("/{id}")
    public void excluir(@PathVariable Long id) {
        usuarioService.excluir(id);
    }
}
