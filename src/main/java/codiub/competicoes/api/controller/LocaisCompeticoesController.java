package codiub.competicoes.api.controller;

import codiub.competicoes.api.DTO.locaisCompeticoes.DadosInsertLocaisCompeticoesRcd;
import codiub.competicoes.api.DTO.locaisCompeticoes.DadosListLocaisCompeticoesRcd;
import codiub.competicoes.api.DTO.locaisCompeticoes.DadosPageLocaisCompeticoesRcd;
import codiub.competicoes.api.DTO.locaisCompeticoes.DadosUpdateLocaisCompeticoesRcd;
import codiub.competicoes.api.entity.LocaisCompeticoes;
import codiub.competicoes.api.entity.TiposModalidades;
import codiub.competicoes.api.filter.LocaisCompeticoesFilter;
import codiub.competicoes.api.filter.TiposModalidadesFilter;
import codiub.competicoes.api.repository.LocaisCompeticoesRepository;
import codiub.competicoes.api.service.LocaisCompeticoesService;
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
@RequestMapping("/locaisCompeticoes")
public class LocaisCompeticoesController {
    @Autowired private LocaisCompeticoesService locaisCompeticoesService;
    @Autowired private LocaisCompeticoesRepository locaisCompeticoesRepository;

    // Listar locais competicoes
    @GetMapping
    public Page<DadosPageLocaisCompeticoesRcd> findall(@PageableDefault (sort={"nome"}) Pageable paginacao){
        return locaisCompeticoesRepository.findAll(paginacao).map(DadosPageLocaisCompeticoesRcd::new);
    }

    @GetMapping("/filter")
    public Page<LocaisCompeticoes> pesquisar(LocaisCompeticoesFilter filter, Pageable pageable) {
        return locaisCompeticoesRepository.filtrar(filter, pageable);
    }

    @GetMapping("/list")
    public List<LocaisCompeticoes> pesquisar(LocaisCompeticoesFilter filter) {
        return locaisCompeticoesRepository.filtrar(filter);

    }

    @GetMapping(value = "/{id}")
    public ResponseEntity findById(@PathVariable Long id){
        LocaisCompeticoes locaisCompeticoes = locaisCompeticoesService.findById(id);
        return ResponseEntity.ok().body(new DadosListLocaisCompeticoesRcd(locaisCompeticoes));
    }

    // Exemplo
    /*public ResponseEntity<List<Fabricante>> findall(){
        List<Fabricante> listFabricante = fabricanteService.findAll();
        return ResponseEntity.ok().body(listFabricante);
    }*/

    // Inserir
    @PostMapping
    @Transactional
    public ResponseEntity insert(@RequestBody @Valid DadosInsertLocaisCompeticoesRcd dados){
        var locaisCompeticoesSalva = locaisCompeticoesService.insert(dados);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/id")
                .buildAndExpand(locaisCompeticoesSalva.getId()).toUri();
        return ResponseEntity.created(uri).body(locaisCompeticoesSalva);
    }

    // ALTERAR
    @Transactional
    @PutMapping(value = "/{id}")
    public ResponseEntity update(@PathVariable @Valid Long id, @RequestBody DadosUpdateLocaisCompeticoesRcd dados){
        var salva = locaisCompeticoesService.update(id, dados);
        return ResponseEntity.ok().body(new DadosListLocaisCompeticoesRcd(salva));
    }

    // DELETAR
    @Transactional
    @DeleteMapping(value = "/{id}")
    public ResponseEntity delete(@PathVariable Long id){
        locaisCompeticoesService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

