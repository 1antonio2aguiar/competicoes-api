package codiub.competicoes.api.controller;

import codiub.competicoes.api.DTO.inscricoes.DadosInsertInscricoesRcd;
import codiub.competicoes.api.DTO.inscricoes.DadosListInscricoesRcd;
import codiub.competicoes.api.DTO.inscricoes.DadosUpdateInscricoesRcd;
import codiub.competicoes.api.entity.Inscricoes;
import codiub.competicoes.api.filter.InscricaoFilter;
import codiub.competicoes.api.repository.InscricaoRepository;
import codiub.competicoes.api.service.InscricaoService;
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
import java.util.List;

@RestController
@RequestMapping("/inscricao")
public class InscricaoController {
    @Autowired
    private InscricaoRepository inscricaoRepository;
    @Autowired
    private InscricaoService inscricaoService;

    // Listar inscrições
    @GetMapping
    public Page<DadosListInscricoesRcd> findall(@PageableDefault(sort = {"id"}) Pageable paginacao) {
        return inscricaoService.findall(paginacao);
    }

    @GetMapping("/list")
    public List<Inscricoes> pesquisar(InscricaoFilter filter ) {
        return inscricaoRepository.filtrar(filter);
    }

    @GetMapping("/filter")
    public Page<DadosListInscricoesRcd> pesquisar(InscricaoFilter filter, Pageable pageable) {
        return inscricaoService.pesquisar(filter, pageable);
    }
    @GetMapping("/{id}")
    public ResponseEntity<DadosListInscricoesRcd> findById(@PathVariable Long id) {
        DadosListInscricoesRcd dados = inscricaoService.findById(id);
        return dados != null
                ? ResponseEntity.ok(dados)
                : ResponseEntity.notFound().build();
    }

    // ALTERAR
    @Transactional
    @PutMapping(value = "/{id}")
    public ResponseEntity update(@PathVariable @Valid Long id, @RequestBody DadosUpdateInscricoesRcd dados){
        var salva = inscricaoService.update(id, dados);
        return ResponseEntity.ok().body(DadosListInscricoesRcd.fromInscricao(salva));
    }

    //INSERT
    @PostMapping
    @Transactional
    public ResponseEntity insert(@RequestBody @Valid DadosInsertInscricoesRcd dados){
        var inscricaoSalva = inscricaoService.insert(dados);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/id")
                .buildAndExpand(inscricaoSalva.getId()).toUri();
        return ResponseEntity.created(uri).body(DadosListInscricoesRcd.fromInscricao(inscricaoSalva));
    }

    // DELETAR
    @Transactional
    @DeleteMapping(value = "/{id}")
    public ResponseEntity delete(@PathVariable Long id){
        inscricaoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}


