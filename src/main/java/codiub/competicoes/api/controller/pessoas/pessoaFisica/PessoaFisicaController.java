package codiub.competicoes.api.controller.pessoas.pessoaFisica;

import codiub.competicoes.api.DTO.pessoas.pessoa.*;
import codiub.competicoes.api.client.PessoaApiClient;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pessoaFisica")
public class PessoaFisicaController {
    @Autowired
    private PessoaApiClient pessoaApiClient;

    @PostMapping
    public ResponseEntity<PessoaApiResponse> insert(@RequestBody @Valid PessoaApiRequest dados) {
        return pessoaApiClient.insertPessoaFisica(dados);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PessoaApiResponse> update(@PathVariable Long id, @RequestBody @Valid PessoaApiRequest dados) {
        return pessoaApiClient.updatePessoaFisica(id, dados);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PessoaApiResponse> findById(@PathVariable Long id) {
        return pessoaApiClient.findPessoaFisicaById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return pessoaApiClient.deletePessoaFisica(id);
    }
}


