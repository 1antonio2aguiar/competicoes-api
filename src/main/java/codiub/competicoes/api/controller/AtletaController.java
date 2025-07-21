package codiub.competicoes.api.controller;

import codiub.competicoes.api.DTO.atletas.DadosAtletasReduzidoRcd;
import codiub.competicoes.api.DTO.atletas.DadosInsertAtletasRcd;
import codiub.competicoes.api.DTO.atletas.DadosListAtletasRcd;
import codiub.competicoes.api.DTO.atletas.DadosUpdateAtletaRcd;
import codiub.competicoes.api.DTO.equipe.DadosInsertEquipeRcd;
import codiub.competicoes.api.DTO.equipe.DadosListEquipeRcd;
import codiub.competicoes.api.DTO.equipe.DadosUpdateEquipeRcd;
import codiub.competicoes.api.DTO.pessoas.DadosPessoasReduzidoRcd;
import codiub.competicoes.api.entity.Atleta;
import codiub.competicoes.api.entity.Equipe;
import codiub.competicoes.api.filter.AtletaFilter;
import codiub.competicoes.api.filter.EquipeFilter;
import codiub.competicoes.api.filter.pessoas.PessoasFilter;
import codiub.competicoes.api.repository.AtletaRepository;
import codiub.competicoes.api.repository.EquipeRepository;
import codiub.competicoes.api.service.AtletaService;
import codiub.competicoes.api.service.EquipeService;
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
@RequestMapping("/atleta")
public class AtletaController {
    @Autowired
    private AtletaRepository atletaRepository;
    @Autowired
    private AtletaService atletaService;

    // Listar atletas
    @GetMapping
    public Page<DadosListAtletasRcd> findall(@PageableDefault(sort = {"nome"}) Pageable paginacao) {
        return atletaService.findall(paginacao);
    }

    @GetMapping("/list")
    public List<Atleta> pesquisar(AtletaFilter filter ) {
        return atletaRepository.filtrar(filter);
    }

    @GetMapping("/filter")
    public Page<DadosListAtletasRcd> pesquisar(AtletaFilter filter, Pageable pageable) {
        return atletaService.pesquisar(filter, pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosListAtletasRcd> findById(@PathVariable Long id) {
        DadosListAtletasRcd dadosListAtletasRcd = atletaService.findById(id);
        return dadosListAtletasRcd != null
                ? ResponseEntity.ok(dadosListAtletasRcd)
                : ResponseEntity.notFound().build();
    }

    @GetMapping("/atletaNotInInscricoes")
    public Page<DadosAtletasReduzidoRcd> pesquisarByAtleta(AtletaFilter filter, Pageable pageable) {
        System.err.println("PARAMETRO " + filter);
        return atletaService.atletaNotInInscricoes(filter, pageable);
    }

    // ALTERAR
    @Transactional
    @PutMapping(value = "/{id}")
    public ResponseEntity update(@PathVariable @Valid Long id, @RequestBody DadosUpdateAtletaRcd dados){
        var salva = atletaService.update(id, dados);
        return ResponseEntity.ok().body(DadosListAtletasRcd.fromAtleta(salva));
    }

    //INSERT
    @PostMapping
    @Transactional
    public ResponseEntity insert(@RequestBody @Valid DadosInsertAtletasRcd dados){
        var atletaSalva = atletaService.insert(dados);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/id")
                .buildAndExpand(atletaSalva.getId()).toUri();
        return ResponseEntity.created(uri).body(DadosListAtletasRcd.fromAtleta(atletaSalva));
    }

    // DELETAR
    @Transactional
    @DeleteMapping(value = "/{id}")
    public ResponseEntity delete(@PathVariable Long id){
        atletaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}


