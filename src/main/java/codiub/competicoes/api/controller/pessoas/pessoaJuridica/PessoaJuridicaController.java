package codiub.competicoes.api.controller.pessoas.pessoaJuridica;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import codiub.competicoes.api.DTO.pessoas.pessoa.PessoaJuridicaApiRequest;
import codiub.competicoes.api.DTO.pessoas.pessoa.PessoaJuridicaApiResponse;
import codiub.competicoes.api.client.PessoaApiClient;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pessoaJuridica") // <<< URL que o Angular vai chamar
public class PessoaJuridicaController {

    @Autowired
    private PessoaApiClient pessoaApiClient;

    @PostMapping
    public ResponseEntity<PessoaJuridicaApiResponse> insert(@RequestBody @Valid PessoaJuridicaApiRequest dados) {
        return pessoaApiClient.insertPessoaJuridica(dados);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PessoaJuridicaApiResponse> update(@PathVariable Long id, @RequestBody @Valid PessoaJuridicaApiRequest dados) {
        return pessoaApiClient.updatePessoaJuridica(id, dados);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return pessoaApiClient.deletePessoaJuridica(id);
    }
}


