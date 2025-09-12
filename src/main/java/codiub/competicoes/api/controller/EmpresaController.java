package codiub.competicoes.api.controller;

import codiub.competicoes.api.DTO.campeonato.DadosListCampeonatoRcd;
import codiub.competicoes.api.DTO.campeonato.DadosUpdateCampeonatoRcd;
import codiub.competicoes.api.DTO.empresa.DadosCadastroEmpresa;
import codiub.competicoes.api.DTO.empresa.DadosListEmpresaRcd;
import codiub.competicoes.api.DTO.empresa.DadosUpdateEmpresa;
import codiub.competicoes.api.entity.Campeonato;
import codiub.competicoes.api.entity.Empresa;
import codiub.competicoes.api.filter.CampeonatoFilter;
import codiub.competicoes.api.filter.EmpresaFilter;
import codiub.competicoes.api.repository.EmpresaRepository;
import codiub.competicoes.api.service.EmpresaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/empresas")
public class EmpresaController {

    @Autowired
    private EmpresaService empresaService;
    @Autowired
    private EmpresaRepository empresaRepository;

    @GetMapping("/filter")
    public Page<Empresa> pesquisar(EmpresaFilter filter, Pageable pageable) {
        return empresaRepository.filtrar(filter, pageable);
    }

    @GetMapping("/list")
    public List<Empresa> pesquisar(EmpresaFilter filter ) {
        return empresaRepository.filtrar(filter);
    }
   @GetMapping
    public ResponseEntity<List<DadosListEmpresaRcd>> listar() {
        List<Empresa> empresas = empresaService.listarTodas();
        List<DadosListEmpresaRcd> dto = empresas.stream()
                .map(DadosListEmpresaRcd::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dto);
    }

    // CREATE (POST)
    @PostMapping
    @Transactional
    public ResponseEntity<DadosListEmpresaRcd> insert(@RequestBody @Valid DadosCadastroEmpresa dados, UriComponentsBuilder uriBuilder) {
        Empresa novaEmpresa = empresaService.insert(dados);
        URI uri = uriBuilder.path("/empresas/{id}").buildAndExpand(novaEmpresa.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosListEmpresaRcd(novaEmpresa));
    }

    // READ (GET by ID)
    @GetMapping("/{id}")
    public ResponseEntity<DadosListEmpresaRcd> buscarPorId(@PathVariable Long id) {
        Empresa empresa = empresaService.buscarPorId(id);
        return ResponseEntity.ok(new DadosListEmpresaRcd(empresa));
    }

    // UPDATE (PUT)
    @Transactional
    @PutMapping(value = "/{id}")
    public ResponseEntity update(@PathVariable @Valid Long id, @RequestBody DadosUpdateEmpresa dados){
        var salva = empresaService.update(id, dados);
        return ResponseEntity.ok().body(new DadosListEmpresaRcd(salva));
    }

    // DELETE
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        empresaService.deletar(id);
        return ResponseEntity.noContent().build(); // Retorna 204 No Content, que é o padrão para delete
    }
}

