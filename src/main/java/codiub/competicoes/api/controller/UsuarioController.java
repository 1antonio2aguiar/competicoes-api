package codiub.competicoes.api.controller;

import codiub.competicoes.api.DTO.usuarios.DadosCadastroUsuario;
import codiub.competicoes.api.entity.seguranca.Usuario;
import codiub.competicoes.api.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<Usuario> cadastrar(@RequestBody @Valid DadosCadastroUsuario dados, UriComponentsBuilder uriBuilder) {
        Usuario novoUsuario = usuarioService.cadastrar(dados);

        // Cria a URI para o novo recurso criado (boa prática REST)
        URI uri = uriBuilder.path("/usuarios/{id}").buildAndExpand(novoUsuario.getId()).toUri();

        // Retorna o status 201 (Created) e o novo usuário no corpo da resposta
        return ResponseEntity.created(uri).body(novoUsuario);
    }
}