package codiub.competicoes.api.controller;

import codiub.competicoes.api.DTO.apuracao.DadosInsertApuracoesRcd;
import codiub.competicoes.api.DTO.apuracao.DadosListApuracaoAndInscricaoRcd;
import codiub.competicoes.api.DTO.apuracao.DadosListApuracoesRcd;
import codiub.competicoes.api.DTO.apuracao.DadosUpdateApuracoesRcd;
import codiub.competicoes.api.DTO.atletas.DadosAtletasReduzidoRcd;
import codiub.competicoes.api.entity.Apuracao;
import codiub.competicoes.api.filter.ApuracaoFilter;
import codiub.competicoes.api.filter.AtletaFilter;
import codiub.competicoes.api.repository.ApuracaoRepository;
import codiub.competicoes.api.service.ApuracaoService;
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
@RequestMapping("/apuracao")
public class ApuracaoController {
    @Autowired
    private ApuracaoRepository apuracaoRepository;
    @Autowired
    private ApuracaoService apuracaoService;

    // Listar apurações
    @GetMapping
    public Page<DadosListApuracoesRcd> findall(@PageableDefault(sort = {"id"}) Pageable paginacao) {
        return apuracaoService.findall(paginacao);
    }

    @GetMapping("/list")
    public List<Apuracao> pesquisar(ApuracaoFilter filter ) {
        return apuracaoRepository.filtrar(filter);
    }

    @GetMapping("/filter")
    public Page<DadosListApuracoesRcd> pesquisar(ApuracaoFilter filter, Pageable pageable) {
        return apuracaoService.pesquisar(filter, pageable);
    }
    @GetMapping("/{id}")
    public ResponseEntity<DadosListApuracoesRcd> findById(@PathVariable Long id) {
        DadosListApuracoesRcd dados = apuracaoService.findById(id);
        return dados != null
                ? ResponseEntity.ok(dados)
                : ResponseEntity.notFound().build();
    }

    @GetMapping("/apuracaoAndInscricao")
    public Page<DadosListApuracaoAndInscricaoRcd> pesquisarByProva(ApuracaoFilter filter, Pageable pageable) {
        return apuracaoService.apuracaoAndInscricao(filter, pageable);
    }

    // ALTERAR
    @Transactional
    @PutMapping(value = "/{id}")
    public ResponseEntity update(@PathVariable @Valid Long id, @RequestBody DadosUpdateApuracoesRcd dados){
        var salva = apuracaoService.update(id, dados);
        return ResponseEntity.ok().body(DadosListApuracoesRcd.fromApuracao(salva));
    }

    //INSERT
    @PostMapping
    @Transactional
    public ResponseEntity insert(@RequestBody @Valid DadosInsertApuracoesRcd dados){
        System.err.println("Dados " + dados);
        var apuracaoSalva = apuracaoService.insert(dados);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/id")
                .buildAndExpand(apuracaoSalva.getId()).toUri();
        return ResponseEntity.created(uri).body(DadosListApuracoesRcd.fromApuracao(apuracaoSalva));
    }

    // DELETAR
    @Transactional
    @DeleteMapping(value = "/{id}")
    public ResponseEntity delete(@PathVariable Long id){
        apuracaoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}


