package codiub.competicoes.api.controller;

import codiub.competicoes.api.DTO.tipoModalidade.DadosInsertTipoModalidadeRcd;
import codiub.competicoes.api.DTO.tipoModalidade.DadosListTipoModalidadeRcd;
import codiub.competicoes.api.DTO.tipoModalidade.DadosPageTipoModalidadeRcd;
import codiub.competicoes.api.DTO.tipoModalidade.DadosUpdateTipoModalidadeRcd;
import codiub.competicoes.api.entity.Etapa;
import codiub.competicoes.api.entity.TiposModalidades;
import codiub.competicoes.api.filter.EtapaFilter;
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
    public Page<DadosPageTipoModalidadeRcd> findall(@PageableDefault (sort={"nome"}) Pageable paginacao){
        return tiposModalidadesRepository.findAll(paginacao).map(DadosPageTipoModalidadeRcd::new);
    }

    /*@GetMapping
    public Page<Etapa> findall(@PageableDefault (sort={"nome"}) EtapaFilter etapaFilter, Pageable paginacao){
        //return etapasRepository.findAll(paginacao).map(DadosListEtapaRcd::new);
        //long id = 6;
        //etapaFilter.getCampeonatoFilter().setId(id);
        System.err.println("Celine Dion ");
        return etapasRepository.filtrar(etapaFilter, paginacao);
    }*/

    // Lista de tipo modalidades com Paginacao e filter
    @GetMapping("/filter")
    public Page<TiposModalidades> pesquisar(TiposModalidadesFilter filter, Pageable pageable) {
        System.err.println("TiposModalidadesFilter " + filter );
        return tiposModalidadesRepository.filtrar(filter, pageable);
    }

    @GetMapping("/list")
    public List<TiposModalidades> pesquisar(TiposModalidadesFilter filter ) {
        return tiposModalidadesRepository.filtrar(filter);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity findById(@PathVariable Long id){
        TiposModalidades tiposModalidades = tiposModalidadesService.findById(id);
        return ResponseEntity.ok().body(new DadosListTipoModalidadeRcd(tiposModalidades));
    }

    // Exemplo
    /*public ResponseEntity<List<Fabricante>> findall(){
        List<Fabricante> listFabricante = fabricanteService.findAll();
        return ResponseEntity.ok().body(listFabricante);
    }*/


    // Inserir
    @PostMapping
    @Transactional
    public ResponseEntity insert(@RequestBody @Valid DadosInsertTipoModalidadeRcd dados){
        var tiposModalidadesSalva = tiposModalidadesService.insert(dados);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/id")
                .buildAndExpand(tiposModalidadesSalva.getId()).toUri();
        return ResponseEntity.created(uri).body(tiposModalidadesSalva);
    }

    // ALTERAR
    @Transactional
    @PutMapping(value = "/{id}")
    public ResponseEntity update(@PathVariable @Valid Long id, @RequestBody DadosUpdateTipoModalidadeRcd dados){
        var salva = tiposModalidadesService.update(id, dados);
        return ResponseEntity.ok().body(new DadosListTipoModalidadeRcd(salva));
    }

    // DELETAR
    @Transactional
    @DeleteMapping(value = "/{id}")
    public ResponseEntity delete(@PathVariable Long id){
        tiposModalidadesService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

