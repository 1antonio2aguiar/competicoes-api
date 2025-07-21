package codiub.competicoes.api.controller;

import codiub.competicoes.api.DTO.categoria.DadosCategoriaRcd;
import codiub.competicoes.api.DTO.tiposNados.DadosTipoNadoRcd;
import codiub.competicoes.api.entity.Categoria;
import codiub.competicoes.api.entity.Etapa;
import codiub.competicoes.api.entity.TipoNado;
import codiub.competicoes.api.filter.CategoriaFilter;
import codiub.competicoes.api.filter.EtapaFilter;
import codiub.competicoes.api.filter.TipoNadoFilter;
import codiub.competicoes.api.repository.CategoriaRepository;
import codiub.competicoes.api.repository.TipoNadoRepository;
import codiub.competicoes.api.service.CategoriaService;
import codiub.competicoes.api.service.TipoNadoService;
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
@RequestMapping("/tiposNado")
public class TipoNadoController {
    @Autowired private TipoNadoService tipoNadoService;
    @Autowired private TipoNadoRepository tipoNadoRepository;

    // Listar tipos dados
    @GetMapping
    public Page<DadosTipoNadoRcd> findall(@PageableDefault (sort={"nome"}) Pageable paginacao){
        return tipoNadoRepository.findAll(paginacao).map(DadosTipoNadoRcd::new);
    }
    @GetMapping("/list")
    public List<TipoNado> pesquisar(TipoNadoFilter filter ) {
        return tipoNadoRepository.filtrar(filter);
    }
    // Lista de tipos dados com Paginacao e filter
    @GetMapping("/filter")
    public Page<TipoNado> pesquisar(TipoNadoFilter filter, Pageable pageable) {
        return tipoNadoRepository.filtrar(filter, pageable);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity findById(@PathVariable Long id){
        TipoNado tipoNado = tipoNadoService.findById(id);
        return ResponseEntity.ok().body(new DadosTipoNadoRcd(tipoNado));
    }

    // Exemplo
    /*public ResponseEntity<List<TipoNado>> findall(){
        List<TipoNado> listTipoNado = tipoNadoService.findAll();
        return ResponseEntity.ok().body(listTipoNado);
    }*/

    // Inserir
    @PostMapping
    @Transactional
    public ResponseEntity insert(@RequestBody @Valid DadosTipoNadoRcd dados){
        var tipoNadoSalva = tipoNadoService.insert(dados);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/id")
                .buildAndExpand(tipoNadoSalva.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosTipoNadoRcd(tipoNadoSalva));
    }

    // ALTERAR
    @Transactional
    @PutMapping(value = "/{id}")
    public ResponseEntity update(@PathVariable @Valid Long id, @RequestBody DadosTipoNadoRcd dados){
        var salva = tipoNadoService.update(id, dados);
        return ResponseEntity.ok().body(new DadosTipoNadoRcd(salva));
    }

    // DELETAR
    @Transactional
    @DeleteMapping(value = "/{id}")
    public ResponseEntity delete(@PathVariable Long id){
        tipoNadoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

