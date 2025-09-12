package codiub.competicoes.api.controller;

import codiub.competicoes.api.DTO.usuarios.DadosCadastroUsuario;
import codiub.competicoes.api.DTO.usuarios.DadosListUsuario;
import codiub.competicoes.api.DTO.usuarios.DadosUpdateUsuario;
import codiub.competicoes.api.entity.seguranca.Usuario;
import codiub.competicoes.api.filter.UsuarioFilter;
import codiub.competicoes.api.repository.seguranca.usuario.UsuarioRepository;
import codiub.competicoes.api.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    UsuarioRepository usuarioRepository;

    @GetMapping("/filter")
    public Page<Usuario> pesquisar(UsuarioFilter filter, Pageable pageable) {
        return usuarioRepository.filtrar(filter, pageable);
    }
    @GetMapping
    public ResponseEntity<List<DadosListUsuario>> listar() {
        List<Usuario> usuarios = usuarioService.findAll();
        List<DadosListUsuario> dto = usuarios.stream()
                .map(DadosListUsuario::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dto);
    }

    // READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<DadosListUsuario> findById(@PathVariable Long id) {
        Usuario usuario = usuarioService.findById(id);
        return ResponseEntity.ok(new DadosListUsuario(usuario));
    }

    // CREATE
    @PostMapping
    @Transactional
    public ResponseEntity<DadosListUsuario> insert(@RequestBody @Valid DadosCadastroUsuario dados, UriComponentsBuilder uriBuilder) {

        Usuario novoUsuario = usuarioService.insert(dados);
        URI uri = uriBuilder.path("/usuarios/{id}").buildAndExpand(novoUsuario.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosListUsuario(novoUsuario));
    }

    // UPDATE
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<DadosListUsuario> update(@PathVariable Long id, @RequestBody @Valid DadosUpdateUsuario dados) {
        Usuario usuarioAtualizado = usuarioService.update(id, dados);

        return ResponseEntity.ok(new DadosListUsuario(usuarioAtualizado));
    }

    // DELETE
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        usuarioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}