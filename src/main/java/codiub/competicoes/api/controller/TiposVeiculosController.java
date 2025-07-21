package codiub.competicoes.api.controller;

import codiub.competicoes.api.DTO.tiposVeiculos.DadosTiposVeiculosRcd;
import codiub.competicoes.api.entity.TiposVeiculos;
import codiub.competicoes.api.filter.TiposVeiculosFilter;
import codiub.competicoes.api.repository.TiposVeiculosRepository;
import codiub.competicoes.api.service.TiposVeiculosService;
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

@RestController
@RequestMapping("/tiposVeiculos")
public class TiposVeiculosController {
    @Autowired private TiposVeiculosService tiposVeiculosService;
    @Autowired private TiposVeiculosRepository tiposVeiculosRepository;

    // Listar tipos veiculo
    @GetMapping
    public Page<DadosTiposVeiculosRcd> findall(@PageableDefault (sort={"descricao"}) Pageable paginacao){
        return tiposVeiculosRepository.findAll(paginacao).map(DadosTiposVeiculosRcd::new);
    }

    // Lista de tipos veiculos com Paginacao e filter
    @GetMapping("/filter")
    public Page<TiposVeiculos> pesquisar(TiposVeiculosFilter filter, Pageable pageable) {
        return tiposVeiculosRepository.filtrar(filter, pageable);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity findById(@PathVariable Long id){
        TiposVeiculos tiposVeiculos = tiposVeiculosService.findById(id);
        return ResponseEntity.ok().body(new DadosTiposVeiculosRcd(tiposVeiculos));
    }

    // Exemplo
    /*public ResponseEntity<List<Fabricante>> findall(){
        List<Fabricante> listFabricante = fabricanteService.findAll();
        return ResponseEntity.ok().body(listFabricante);
    }*/


    // Inserir
    @PostMapping
    @Transactional
    public ResponseEntity insert(@RequestBody @Valid DadosTiposVeiculosRcd dados){
        var tiposVeiculosSalva = tiposVeiculosService.insert(dados);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/id")
                .buildAndExpand(tiposVeiculosSalva.getId()).toUri();
        return ResponseEntity.created(uri).body(tiposVeiculosSalva);
    }

    // ALTERAR
    @Transactional
    @PutMapping(value = "/{id}")
    public ResponseEntity update(@PathVariable @Valid Long id, @RequestBody DadosTiposVeiculosRcd dados){
        var salva = tiposVeiculosService.update(id, dados);
        return ResponseEntity.ok().body(new DadosTiposVeiculosRcd(salva));
    }

    // DELETAR
    @Transactional
    @DeleteMapping(value = "/{id}")
    public ResponseEntity delete(@PathVariable Long id){
        tiposVeiculosService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

