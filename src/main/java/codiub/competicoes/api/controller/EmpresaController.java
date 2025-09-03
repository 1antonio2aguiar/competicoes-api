package codiub.competicoes.api.controller;

import codiub.competicoes.api.DTO.empresa.DadosCadastroEmpresa;
import codiub.competicoes.api.DTO.empresa.DadosListEmpresaRcd;
import codiub.competicoes.api.entity.Empresa;
import codiub.competicoes.api.service.EmpresaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/empresas")
public class EmpresaController {

    @Autowired
    private EmpresaService empresaService;

    @PostMapping
    public ResponseEntity<DadosListEmpresaRcd> cadastrar(@RequestBody @Valid DadosCadastroEmpresa dados, UriComponentsBuilder uriBuilder) {
        Empresa novaEmpresa = empresaService.cadastrar(dados);
        URI uri = uriBuilder.path("/empresas/{id}").buildAndExpand(novaEmpresa.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosListEmpresaRcd(novaEmpresa));
    }

    @GetMapping
    public ResponseEntity<List<DadosListEmpresaRcd>> listar() {
        List<Empresa> empresas = empresaService.listarTodas();
        List<DadosListEmpresaRcd> dto = empresas.stream()
                .map(DadosListEmpresaRcd::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dto);
    }
}