package codiub.competicoes.api.controller;

import codiub.competicoes.api.DTO.etapa.DadosInsertEtapaRcd;
import codiub.competicoes.api.DTO.etapa.DadosListEtapaRcd;
import codiub.competicoes.api.DTO.etapa.DadosUpdateEtapaRcd;
import codiub.competicoes.api.entity.Etapa;
import codiub.competicoes.api.filter.EtapaFilter;
import codiub.competicoes.api.repository.EtapaRepository;
import codiub.competicoes.api.service.EtapaService;
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
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/etapas")
public class EtapaController {
    @Autowired private EtapaService etapaService;
    @Autowired private EtapaRepository etapasRepository;

    // Listar etapas
    @GetMapping
    public Page<Etapa> findall(@PageableDefault (sort={"nome"}) EtapaFilter etapaFilter, Pageable paginacao){
        return etapasRepository.filtrar(etapaFilter, paginacao);
    }

    @GetMapping("/filter")
    public Page<Etapa> pesquisar(EtapaFilter filter, Pageable pageable) {
        return etapasRepository.filtrar(filter, pageable);
    }
    @GetMapping("/list")
    public List<Etapa> pesquisar(EtapaFilter filter ) {
        return etapasRepository.filtrar(filter);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity findById(@PathVariable Long id){
        Etapa etapa = etapaService.findById(id);
        return ResponseEntity.ok().body(new DadosListEtapaRcd(etapa));
    }

    // Exemplo
    /*public ResponseEntity<List<Campeonato>> findall(){
        List<Campeonato> listCampeonato = campeonatoService.findAll();
        return ResponseEntity.ok().body(listCampeonato);
    }*/

    // Inserir
    @PostMapping
    @Transactional
    public ResponseEntity insert(@RequestBody @Valid DadosInsertEtapaRcd dados){
        var etapaSalva = etapaService.insert(dados);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/id")
                .buildAndExpand(etapaSalva.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosListEtapaRcd(etapaSalva));
    }

    // ALTERAR
    @Transactional
    @PutMapping(value = "/{id}")
    public ResponseEntity update(@PathVariable @Valid Long id, @RequestBody DadosUpdateEtapaRcd dados){
        System.err.println("Dados " + dados);
        var salva = etapaService.update(id, dados);
        return ResponseEntity.ok().body(new DadosListEtapaRcd(salva));
    }

    // DELETAR
    @Transactional
    @DeleteMapping(value = "/{id}")
    public ResponseEntity delete(@PathVariable Long id){
        etapaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

