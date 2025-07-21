package codiub.competicoes.api.controller;

import codiub.competicoes.api.DTO.equipe.DadosInsertEquipeRcd;
import codiub.competicoes.api.DTO.equipe.DadosListEquipeRcd;
import codiub.competicoes.api.DTO.equipe.DadosUpdateEquipeRcd;
import codiub.competicoes.api.DTO.etapa.DadosInsertEtapaRcd;
import codiub.competicoes.api.DTO.etapa.DadosListEtapaRcd;
import org.springframework.transaction.annotation.Transactional;
import codiub.competicoes.api.entity.Equipe;
import codiub.competicoes.api.filter.EquipeFilter;
import codiub.competicoes.api.repository.EquipeRepository;
import codiub.competicoes.api.service.EquipeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/equipe")
public class EquipeController {
    @Autowired
    private EquipeRepository equipeRepository;
    @Autowired
    private EquipeService equipeService;

    // Listar equipes
    @GetMapping
    public Page<DadosListEquipeRcd> findall(@PageableDefault(sort = {"nome"}) Pageable paginacao) {
        return equipeService.findall(paginacao);
    }

    @GetMapping("/list")
    public List<Equipe> pesquisar(EquipeFilter filter ) {
        return equipeRepository.filtrar(filter);
    }

    @GetMapping("/filter")
    public Page<DadosListEquipeRcd> pesquisar(EquipeFilter filter, Pageable pageable) {
        return equipeService.pesquisar(filter, pageable);
    }
    @GetMapping("/{id}")
    public ResponseEntity<DadosListEquipeRcd> findById(@PathVariable Long id) {
        DadosListEquipeRcd dadosEquipeRcd = equipeService.findById(id);
        return dadosEquipeRcd != null
                ? ResponseEntity.ok(dadosEquipeRcd)
                : ResponseEntity.notFound().build();
    }

    // ALTERAR
   /* @Transactional
    @PutMapping(value = "/{id}")
    public ResponseEntity update(@PathVariable @Valid Long id, @RequestBody DadosUpdateEquipeRcd dados){
        var salva = equipeService.update(id, dados);
        return ResponseEntity.ok().body(DadosListEquipeRcd.fromEquipe(salva));
    }*/

    //INSERT
    /*@PostMapping
    @Transactional
    public ResponseEntity insert(@RequestBody @Valid DadosInsertEquipeRcd dados){
        var equipeSalva = equipeService.insert(dados);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/id")
                .buildAndExpand(equipeSalva.getId()).toUri();
        return ResponseEntity.created(uri).body(DadosListEquipeRcd.fromEquipe(equipeSalva));
    }*/

    // DELETAR
    @Transactional
    @DeleteMapping(value = "/{id}")
    public ResponseEntity delete(@PathVariable Long id){
        equipeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}


