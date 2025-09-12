package codiub.competicoes.api.service;

import codiub.competicoes.api.DTO.perfis.DadosListPerfil;
import codiub.competicoes.api.entity.seguranca.Perfil;
import codiub.competicoes.api.repository.EmpresaRepository;
import codiub.competicoes.api.repository.seguranca.perfil.PerfilRepository;
import codiub.competicoes.api.repository.seguranca.usuario.UsuarioRepository;
import codiub.competicoes.api.service.exceptions.ObjectNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PerfilService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PerfilRepository perfilRepository;
    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<Perfil> findAll() {
        return perfilRepository.findAll();
    }
    @Transactional(readOnly = true)
    public Perfil findById(Long id) {
        return perfilRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Perfil não encontrado com o ID: " + id));
    }

    // Insert
    @Transactional
    public Perfil insert(DadosListPerfil dados) {
        if (perfilRepository.findByNome(dados.nome()).isPresent()) {
            throw new IllegalArgumentException("Já existe um perfil com o nome: " + dados.nome());
        }
        Perfil novoPerfil = new Perfil();
        BeanUtils.copyProperties(dados, novoPerfil, "id");
        return perfilRepository.save(novoPerfil);
    }
    @Transactional
    public Perfil update(Long id, DadosListPerfil dados){
        Perfil perfilUpd = perfilRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Perfil não cadastrada. Id: " + id));

        BeanUtils.copyProperties(dados, perfilUpd, "id");
        return perfilRepository.save(perfilUpd);
    }
    @Transactional
    public void delete(Long id) {
        if (!perfilRepository.existsById(id)) {
            throw new EntityNotFoundException("Perfil não encontrado com o ID: " + id);
        }
        perfilRepository.deleteById(id);
    }

}
