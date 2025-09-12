package codiub.competicoes.api.controller;

import codiub.competicoes.api.DTO.DadosLogin;
import codiub.competicoes.api.DTO.DadosTokenJWT;
import codiub.competicoes.api.commom.TokenService;
import codiub.competicoes.api.entity.seguranca.Usuario;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService; // Agora esta é a SUA classe TokenService

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid DadosLogin dados) {

        //System.err.println("Dados " + dados);

        var authenticationToken = new UsernamePasswordAuthenticationToken(dados.email(), dados.password());

        var authentication = authenticationManager.authenticate(authenticationToken);

        // Agora o erro desaparece, pois tokenService é a sua classe e tem o método gerarToken
        var tokenJWT = tokenService.gerarToken((Usuario) authentication.getPrincipal());

        return ResponseEntity.ok(new DadosTokenJWT(tokenJWT));
    }
}