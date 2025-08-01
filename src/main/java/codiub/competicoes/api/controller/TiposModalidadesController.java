package codiub.competicoes.api.controller;

import codiub.competicoes.api.DTO.provas.DadosListProvasRcd;
import codiub.competicoes.api.DTO.tipoModalidade.DadosListModalidadesRcd;
import codiub.competicoes.api.DTO.tipoModalidade.DadosModalidadeRcd;
import codiub.competicoes.api.entity.TiposModalidades;
import codiub.competicoes.api.filter.ProvaFilter;
import codiub.competicoes.api.filter.TiposModalidadesFilter;
import codiub.competicoes.api.repository.TiposModalidadesRepository;
import codiub.competicoes.api.service.TiposModalidadesService;

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
@RequestMapping("/tiposModalidades")
public class TiposModalidadesController {
    @Autowired private TiposModalidadesService tiposModalidadesService;
    @Autowired private TiposModalidadesRepository tiposModalidadesRepository;

    // Listar tipos modalidade
    @GetMapping
    public Page<DadosListModalidadesRcd> findall(@PageableDefault(sort = {"id"}) Pageable paginacao) {
        return tiposModalidadesService.findall(paginacao);
    }

    // Lista de tipo modalidades com Paginacao e filter
    @GetMapping("/filter")
    public Page<DadosListModalidadesRcd> pesquisar(TiposModalidadesFilter filter, Pageable pageable) {
        //System.err.println("TiposModalidadesFilter " + filter );
        return tiposModalidadesService.pesquisar(filter, pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosListModalidadesRcd> findById(@PathVariable Long id) {
        DadosListModalidadesRcd dados = tiposModalidadesService.findById(id);
        return dados != null
                ? ResponseEntity.ok(dados)
                : ResponseEntity.notFound().build();
    }

    // Inserir
    @PostMapping
    @Transactional
    public ResponseEntity insert(@RequestBody @Valid DadosModalidadeRcd dados){
        var tiposModalidadesSalva = tiposModalidadesService.insert(dados);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/id")
                .buildAndExpand(tiposModalidadesSalva.getId()).toUri();
        return ResponseEntity.created(uri).body(tiposModalidadesSalva);
    }

    // ALTERAR
    @Transactional
    @PutMapping(value = "/{id}")
    public ResponseEntity update(@PathVariable @Valid Long id, @RequestBody DadosModalidadeRcd dados){
        var salva = tiposModalidadesService.update(id, dados);
        return ResponseEntity.ok().body( DadosListModalidadesRcd.fromTiposModalidades(salva));
    }

    // DELETAR
    @Transactional
    @DeleteMapping(value = "/{id}")
    public ResponseEntity delete(@PathVariable Long id){
        tiposModalidadesService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

