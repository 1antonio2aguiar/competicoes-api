package codiub.competicoes.api.controller;

import codiub.competicoes.api.DTO.categoria.DadosCategoriaRcd;
import codiub.competicoes.api.entity.Categoria;
import codiub.competicoes.api.entity.TiposModalidades;
import codiub.competicoes.api.filter.CategoriaFilter;
import codiub.competicoes.api.filter.TiposModalidadesFilter;
import codiub.competicoes.api.repository.CategoriaRepository;
import codiub.competicoes.api.service.CategoriaService;
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
@RequestMapping("/categorias")
public class CategoriaController {
    @Autowired private CategoriaService categoriaService;
    @Autowired private CategoriaRepository categoriaRepository;

    // Listar categorias
    @GetMapping
    public Page<DadosCategoriaRcd> findall(@PageableDefault (sort={"id"}) Pageable paginacao){
        return categoriaRepository.findAll(paginacao).map(DadosCategoriaRcd::new);
    }

    // Lista de categoria com Paginacao e filter
    @GetMapping("/filter")
    public Page<Categoria> pesquisar(CategoriaFilter filter, Pageable pageable) {
        return categoriaRepository.filtrar(filter, pageable);
    }

    @GetMapping("/list")
    public List<Categoria> pesquisar(CategoriaFilter filter ) {
        return categoriaRepository.filtrar(filter);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity findById(@PathVariable Long id){
        Categoria categoria = categoriaService.findById(id);
        return ResponseEntity.ok().body(new DadosCategoriaRcd(categoria));
    }

    // Exemplo
    /*public ResponseEntity<List<Campeonato>> findall(){
        List<Campeonato> listCampeonato = campeonatoService.findAll();
        return ResponseEntity.ok().body(listCampeonato);
    }*/

    // Inserir
    @PostMapping
    @Transactional
    public ResponseEntity insert(@RequestBody @Valid DadosCategoriaRcd dados){
        var categoriaSalva = categoriaService.insert(dados);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/id")
                .buildAndExpand(categoriaSalva.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosCategoriaRcd(categoriaSalva));
    }

    // ALTERAR
    @Transactional
    @PutMapping(value = "/{id}")
    public ResponseEntity update(@PathVariable @Valid Long id, @RequestBody DadosCategoriaRcd dados){
        var salva = categoriaService.update(id, dados);
        return ResponseEntity.ok().body(new DadosCategoriaRcd(salva));
    }

    // DELETAR
    @Transactional
    @DeleteMapping(value = "/{id}")
    public ResponseEntity delete(@PathVariable Long id){
        categoriaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

