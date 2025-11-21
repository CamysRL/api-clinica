package br.edu.ifsp.clinica_api.service;

import br.edu.ifsp.clinica_api.model.Medico;
import br.edu.ifsp.clinica_api.model.Paciente;
import br.edu.ifsp.clinica_api.model.Recepcionista;
import br.edu.ifsp.clinica_api.model.Usuario;
import br.edu.ifsp.clinica_api.model.enums.Papel;
import br.edu.ifsp.clinica_api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder encoder;

    public Usuario criarUsuario(String email, String senhaPura, Long idReferencia, Papel papel) {

        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setSenha(encoder.encode(senhaPura));
        usuario.setAtivo(true);
        usuario.setId_referencia(idReferencia);
        usuario.setPapel(papel);
        usuario.setDataCriacao(LocalDate.now());

        return usuarioRepository.save(usuario);
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public void excluir(Long id) {
        usuarioRepository.deleteById(id);
    }
}
