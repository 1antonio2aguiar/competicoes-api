package codiub.competicoes.api.service;

import codiub.competicoes.api.DTO.usuarios.DadosCadastroUsuario;
import codiub.competicoes.api.DTO.usuarios.DadosUpdateUsuario;
import codiub.competicoes.api.entity.Empresa;
import codiub.competicoes.api.entity.seguranca.Perfil;
import codiub.competicoes.api.entity.seguranca.Usuario;
import codiub.competicoes.api.repository.EmpresaRepository;
import codiub.competicoes.api.repository.seguranca.perfil.PerfilRepository;
import codiub.competicoes.api.repository.seguranca.usuario.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
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
    private EmpresaRepository empresaRepository;

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

    @Transactional(readOnly = true)
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Usuario findById(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com o ID: " + id));
    }

    // Insert
    @Transactional
    public Usuario insert(DadosCadastroUsuario dados) {
        if (usuarioRepository.existsByEmail(dados.email())) {
            throw new IllegalArgumentException("O e-mail informado já está em uso.");
        }

        Usuario novoUsuario = new Usuario();
        atualizarDados(novoUsuario, dados.empresaId(), dados.nome(), dados.email(), dados.perfisIds()); // Usa método auxiliar
        novoUsuario.setSenha(passwordEncoder.encode(dados.senha()));
        novoUsuario.setAtivo(true);
        return usuarioRepository.save(novoUsuario);
    }

    @Transactional
    public Usuario update(Long id, DadosUpdateUsuario dados) {
        Usuario usuario = findById(id);
        atualizarDados(usuario, dados.empresaId(), dados.nome(), dados.email(), dados.perfisIds());
        if (dados.ativo() != null) {
            usuario.setAtivo(dados.ativo());
        }
        return usuarioRepository.save(usuario);
    }
    @Transactional
    public void delete(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new EntityNotFoundException("Usuário não encontrado com o ID: " + id);
        }
        usuarioRepository.deleteById(id);
    }

    /**
     * Método auxiliar privado para evitar duplicação de código
     * ao setar dados em create e update.
     */
    private void atualizarDados(Usuario usuario, Long empresaId, String nome, String email, List<Long> perfisIds) {
        usuario.setNome(nome);
        usuario.setEmail(email);

        if (empresaId != null) {
            Empresa empresa = empresaRepository.findById(empresaId)
                    .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada com o ID: " + empresaId));
            usuario.setEmpresa(empresa);
        }

        if (perfisIds != null && !perfisIds.isEmpty()) {
            List<Perfil> perfis = perfilRepository.findAllById(perfisIds);
            usuario.getPerfis().clear(); // Limpa os perfis antigos
            usuario.getPerfis().addAll(perfis); // Adiciona os novos perfis
        }
    }

}
