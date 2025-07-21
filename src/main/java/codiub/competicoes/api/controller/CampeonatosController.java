package codiub.competicoes.api.controller;

import codiub.competicoes.api.DTO.campeonato.DadosInsertCampeonatoRcd;
import codiub.competicoes.api.DTO.campeonato.DadosListCampeonatoRcd;
import codiub.competicoes.api.DTO.campeonato.DadosUpdateCampeonatoRcd;
import codiub.competicoes.api.entity.Campeonato;
import codiub.competicoes.api.filter.CampeonatoFilter;
import codiub.competicoes.api.repository.CampeonatoRepository;
import codiub.competicoes.api.service.CampeonatoService;
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
@RequestMapping("/campeonatos")
public class CampeonatosController {
    @Autowired private CampeonatoService campeonatoService;
    @Autowired private CampeonatoRepository campeonatoRepository;

    // Listar campeonatos
    @GetMapping
    public Page<DadosListCampeonatoRcd> findall(@PageableDefault (sort={"nome"}) Pageable paginacao){
        return campeonatoRepository.findAll(paginacao).map(DadosListCampeonatoRcd::new);
    }

    // Lista de tipos veiculos com Paginacao e filter
    @GetMapping("/filter")
    public Page<Campeonato> pesquisar(CampeonatoFilter filter, Pageable pageable) {
        return campeonatoRepository.filtrar(filter, pageable);
    }

    @GetMapping("/list")
    public List<Campeonato> pesquisar(CampeonatoFilter filter ) {
        return campeonatoRepository.filtrar(filter);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity findById(@PathVariable Long id){
        Campeonato campeonato = campeonatoService.findById(id);
        return ResponseEntity.ok().body(new DadosListCampeonatoRcd(campeonato));
    }

    // Exemplo
    /*public ResponseEntity<List<Campeonato>> findall(){
        List<Campeonato> listCampeonato = campeonatoService.findAll();
        return ResponseEntity.ok().body(listCampeonato);
    }*/

    // Inserir
    @PostMapping
    @Transactional
    public ResponseEntity insert(@RequestBody @Valid DadosInsertCampeonatoRcd dados){
        var campeonatoSalva = campeonatoService.insert(dados);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/id")
                .buildAndExpand(campeonatoSalva.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosListCampeonatoRcd(campeonatoSalva));
    }

    // ALTERAR
    @Transactional
    @PutMapping(value = "/{id}")
    public ResponseEntity update(@PathVariable @Valid Long id, @RequestBody DadosUpdateCampeonatoRcd dados){
        var salva = campeonatoService.update(id, dados);
        return ResponseEntity.ok().body(new DadosListCampeonatoRcd(salva));
    }

    // DELETAR
    @Transactional
    @DeleteMapping(value = "/{id}")
    public ResponseEntity delete(@PathVariable Long id){
        campeonatoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

