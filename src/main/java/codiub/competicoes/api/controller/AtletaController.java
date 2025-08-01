package codiub.competicoes.api.controller;

import codiub.competicoes.api.DTO.atletas.DadosAtletasReduzidoRcd;
import codiub.competicoes.api.DTO.atletas.DadosInsertAtletasRcd;
import codiub.competicoes.api.DTO.atletas.DadosListAtletasRcd;
import codiub.competicoes.api.DTO.atletas.DadosUpdateAtletaRcd;
import codiub.competicoes.api.DTO.pessoas.DadosPessoasReduzidoRcd;
import codiub.competicoes.api.entity.Atleta;
import codiub.competicoes.api.filter.AtletaFilter;
import codiub.competicoes.api.filter.pessoas.PessoaFilter;
import codiub.competicoes.api.filter.pessoas.PessoaFisicaFilter;
import codiub.competicoes.api.filter.pessoas.PessoasFilter;
import codiub.competicoes.api.repository.AtletaRepository;
import codiub.competicoes.api.service.AtletaService;
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
@RequestMapping("/atleta")
public class AtletaController {
    @Autowired
    private AtletaRepository atletaRepository;
    @Autowired
    private AtletaService atletaService;

    @GetMapping("/disponiveis-para-cadastro")
    public Page<DadosPessoasReduzidoRcd> pesquisarPessoasDisponiveis(
            PessoaFisicaFilter filter, // Use o filter adequado
            Pageable pageable
    ) {
        return atletaService.pesquisarPessoasDisponiveisParaAtleta(filter, pageable);
    }
    @GetMapping("/filter")
        public Page<DadosListAtletasRcd> pesquisar(AtletaFilter filter, Pageable pageable) {
            return atletaService.pesquisar(filter, pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosListAtletasRcd> findById(@PathVariable Long id) {
        DadosListAtletasRcd dadosListAtletasRcd = atletaService.findById(id);
        return dadosListAtletasRcd != null
                ? ResponseEntity.ok(dadosListAtletasRcd)
                : ResponseEntity.notFound().build();
    }

    // ALTERAR
    @Transactional
    @PutMapping(value = "/{id}")
    public ResponseEntity update(@PathVariable @Valid Long id, @RequestBody DadosUpdateAtletaRcd dados){
        var salva = atletaService.update(id, dados);
        return ResponseEntity.ok().body(DadosListAtletasRcd.fromAtleta(salva,null));
    }

    //INSERT
    @PostMapping
    @Transactional
    public ResponseEntity insert(@RequestBody @Valid DadosInsertAtletasRcd dados){
        var atletaSalva = atletaService.insert(dados);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/id")
                .buildAndExpand(atletaSalva.getId()).toUri();
        return ResponseEntity.created(uri).body(DadosListAtletasRcd.fromAtleta(atletaSalva, null));
    }

    // DELETAR
    @Transactional
    @DeleteMapping(value = "/{id}")
    public ResponseEntity delete(@PathVariable Long id){
        atletaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}


