package codiub.competicoes.api.controller;

import codiub.competicoes.api.DTO.pontuacao.DadosPontuacaoRcd;
import codiub.competicoes.api.entity.Pontuacao;
import codiub.competicoes.api.filter.PontuacaoFilter;
import codiub.competicoes.api.repository.PontuacaoRepository;
import codiub.competicoes.api.service.PontuacaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/pontuacao")
public class PontuacaoController {
    @Autowired private PontuacaoService pontuacaoService;
    @Autowired private PontuacaoRepository pontuacaoRepository;

    // Listar pontuacaos
    @GetMapping
    public Page<DadosPontuacaoRcd> findall(@PageableDefault (sort={"nome"}) Pageable paginacao){
        return pontuacaoRepository.findAll(paginacao).map(DadosPontuacaoRcd::new);
    }

    // Lista de pontuacao com Paginacao e filter
    @GetMapping("/filter")
    public Page<Pontuacao> pesquisar(PontuacaoFilter filter, Pageable pageable) {
        System.err.println("ProvaFilter " + filter );
        return pontuacaoRepository.filtrar(filter, pageable);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity findById(@PathVariable Long id){
        Pontuacao pontuacao = pontuacaoService.findById(id);
        return ResponseEntity.ok().body(new DadosPontuacaoRcd(pontuacao));
    }

    // Exemplo
    /*public ResponseEntity<List<Campeonato>> findall(){
        List<Campeonato> listCampeonato = campeonatoService.findAll();
        return ResponseEntity.ok().body(listCampeonato);
    }*/

    // Inserir
    @PostMapping
    @Transactional
    public ResponseEntity insert(@RequestBody @Valid DadosPontuacaoRcd dados){
        var pontuacaoSalva = pontuacaoService.insert(dados);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/id")
                .buildAndExpand(pontuacaoSalva.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosPontuacaoRcd(pontuacaoSalva));
    }

    // ALTERAR
    @Transactional
    @PutMapping(value = "/{id}")
    public ResponseEntity update(@PathVariable @Valid Long id, @RequestBody DadosPontuacaoRcd dados){
        var salva = pontuacaoService.update(id, dados);
        return ResponseEntity.ok().body(new DadosPontuacaoRcd(salva));
    }

    // DELETAR
    @Transactional
    @DeleteMapping(value = "/{id}")
    public ResponseEntity delete(@PathVariable Long id){
        pontuacaoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

