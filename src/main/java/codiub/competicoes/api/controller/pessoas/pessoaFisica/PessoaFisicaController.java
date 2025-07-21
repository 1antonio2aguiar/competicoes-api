package codiub.competicoes.api.controller.pessoas.pessoaFisica;

import codiub.competicoes.api.DTO.equipe.DadosInsertEquipeRcd;
import codiub.competicoes.api.DTO.equipe.DadosListEquipeRcd;
import codiub.competicoes.api.DTO.equipe.DadosUpdateEquipeRcd;
import codiub.competicoes.api.DTO.pessoas.DadosPessoasRcd;
import codiub.competicoes.api.DTO.pessoas.DadosPessoasReduzidoRcd;
import codiub.competicoes.api.DTO.pessoas.pessoa.DadosInsertPessoaFisicaRcd;
import codiub.competicoes.api.DTO.pessoas.pessoa.DadosListPessoaFisicaRcd;
import codiub.competicoes.api.DTO.pessoas.pessoa.DadosPessoaFisicaReduzRcd;
import codiub.competicoes.api.config.PessoaApiResponseDto;
import codiub.competicoes.api.filter.pessoas.PessoaFisicaFilter;
import codiub.competicoes.api.filter.pessoas.PessoasFilter;
import codiub.competicoes.api.repository.pessoas.PessoaFisicaRepository;
import codiub.competicoes.api.repository.pessoas.PessoasRepository;
import codiub.competicoes.api.service.pessoas.PessoaFisicaService;
import codiub.competicoes.api.service.pessoas.PessoasService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/pessoaFisica")
public class PessoaFisicaController {
    @Autowired
    private PessoaFisicaRepository pessoaFisicaRepository;
    @Autowired
    private PessoaFisicaService pessoaFisicaService;

    // Listar pessoas
    /*@GetMapping
    public Page<DadosPessoasRcd> findall(@PageableDefault(sort = {"nome"}) Pageable paginacao) {
        return pessoasService.findall(paginacao);
    }*/

    @GetMapping
    @Transactional(readOnly = true)
    public Page<DadosListPessoaFisicaRcd> findall(@PageableDefault (sort={"nome"}) Pageable paginacao){
        return pessoaFisicaRepository.findAll(paginacao).map(DadosListPessoaFisicaRcd::fromPessoaFisica);
    }
    @GetMapping("/filter")
    @Transactional(readOnly = true)
    public Page<DadosPessoaFisicaReduzRcd> pesquisar(PessoaFisicaFilter filter, Pageable pageable) {
        return pessoaFisicaService.pesquisar(filter, pageable);
    }

    /*
        Este metodo filtra/traz apenas pessoas que não pertencem a nenhuma equipe.
        ele esta sendo usado na tela de cadastro de atletas, onde um mesmo atleta não pode pertencer a mais de uma equipe.
     */
    @GetMapping("/pessoaFisicaNotInEquipes")
    @Transactional(readOnly = true)
    public Page<DadosPessoaFisicaReduzRcd> pesquisarByPessoa(PessoaFisicaFilter filter, Pageable pageable) {
        return pessoaFisicaService.pessoaFisicaNotInEquipes(filter, pageable);
    }
    @GetMapping("/{id}")
    public ResponseEntity<DadosListPessoaFisicaRcd> findById(@PathVariable Long id) {
        DadosListPessoaFisicaRcd pessoaDto = pessoaFisicaService.findById(id);
        if (pessoaDto != null) {
            return ResponseEntity.ok(pessoaDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //Insert
    @PostMapping
    public ResponseEntity<DadosListPessoaFisicaRcd> insert(
            @RequestBody @Valid DadosInsertPessoaFisicaRcd dados,
            UriComponentsBuilder uriBuilder) {

        DadosListPessoaFisicaRcd pessoaSalvaDto = pessoaFisicaService.insert(dados);

        URI uri = uriBuilder.path("/pessoaFisica/{id}")
                .buildAndExpand(pessoaSalvaDto.id())
                .toUri();

        return ResponseEntity.created(uri).body(pessoaSalvaDto);
    }

    // ALTERAR
    /*@Transactional
    @PutMapping(value = "/{id}")
    public ResponseEntity update(@PathVariable @Valid Long id, @RequestBody DadosUpdateEquipeRcd dados){
        var salva = equipeService.update(id, dados);
        return ResponseEntity.ok().body(DadosListEquipeRcd.fromEquipe(salva));
    }*/

    // DELETAR
    @Transactional
    @DeleteMapping(value = "/{id}")
    public ResponseEntity delete(@PathVariable Long id){
        pessoaFisicaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}


