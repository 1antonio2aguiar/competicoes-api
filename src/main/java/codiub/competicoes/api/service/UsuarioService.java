package codiub.competicoes.api.service;

import codiub.competicoes.api.DTO.usuarios.DadosCadastroUsuario;
import codiub.competicoes.api.entity.seguranca.Perfil;
import codiub.competicoes.api.entity.seguranca.Usuario;
import codiub.competicoes.api.repository.seguranca.PerfilRepository;
import codiub.competicoes.api.repository.seguranca.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PerfilRepository perfilRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public Usuario cadastrar(DadosCadastroUsuario dados) {
        if (usuarioRepository.existsByEmail(dados.email())) {
            throw new IllegalArgumentException("O e-mail informado já está em uso.");
        }

        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(dados.nome());
        novoUsuario.setEmail(dados.email());
        novoUsuario.setSenha(passwordEncoder.encode(dados.senha()));
        novoUsuario.setAtivo(true);

        // ==========================================================
        // ESTA PARTE AGORA VAI FUNCIONAR CORRETAMENTE
        // ==========================================================
        if (dados.perfisIds() != null && !dados.perfisIds().isEmpty()) {
            // 1. Busca no banco todos os perfis cujos IDs foram enviados na lista
            List<Perfil> perfis = perfilRepository.findAllById(dados.perfisIds());

            // 2. Associa a lista de perfis encontrados ao novo usuário
            novoUsuario.setPerfis(perfis);
        }

        return usuarioRepository.save(novoUsuario);
    }
}
