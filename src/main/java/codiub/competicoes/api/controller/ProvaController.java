package codiub.competicoes.api.controller;

import codiub.competicoes.api.DTO.provas.DadosInsertProvasRcd;
import codiub.competicoes.api.DTO.provas.DadosListProvasRcd;
import codiub.competicoes.api.DTO.provas.DadosUpdateProvasRcd;
import codiub.competicoes.api.entity.Prova;
import codiub.competicoes.api.filter.ProvaFilter;
import codiub.competicoes.api.repository.ProvaRepository;
import codiub.competicoes.api.service.ProvaService;
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
@RequestMapping("/prova")
public class ProvaController {
    @Autowired
    private ProvaRepository provaRepository;
    @Autowired
    private ProvaService provaService;

    // Listar provas
    @GetMapping
    public Page<DadosListProvasRcd> findall(@PageableDefault(sort = {"id"}) Pageable paginacao) {
        return provaService.findall(paginacao);
    }

    @GetMapping("/list")
    public List<Prova> pesquisar(ProvaFilter filter ) {
        return provaRepository.filtrar(filter);
    }

    @GetMapping("/filter")
    public Page<DadosListProvasRcd> pesquisar(ProvaFilter filter, Pageable pageable) {
        return provaService.pesquisar(filter, pageable);
    }
    @GetMapping("/{id}")
    public ResponseEntity<DadosListProvasRcd> findById(@PathVariable Long id) {
        DadosListProvasRcd dados = provaService.findById(id);
        return dados != null
                ? ResponseEntity.ok(dados)
                : ResponseEntity.notFound().build();
    }

    // ALTERAR
    @Transactional
    @PutMapping(value = "/{id}")
    public ResponseEntity update(@PathVariable @Valid Long id, @RequestBody DadosUpdateProvasRcd dados){
        var salva = provaService.update(id, dados);
        return ResponseEntity.ok().body(DadosListProvasRcd.fromProva(salva));
    }

    //INSERT
    @PostMapping
    @Transactional
    public ResponseEntity insert(@RequestBody @Valid DadosInsertProvasRcd dados){
        var provaSalva = provaService.insert(dados);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/id")
                .buildAndExpand(provaSalva.getId()).toUri();
        return ResponseEntity.created(uri).body(DadosListProvasRcd.fromProva(provaSalva));
    }

    // DELETAR
    @Transactional
    @DeleteMapping(value = "/{id}")
    public ResponseEntity delete(@PathVariable Long id){
        provaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}


