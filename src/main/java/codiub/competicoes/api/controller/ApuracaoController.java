package codiub.competicoes.api.controller;

import codiub.competicoes.api.DTO.apuracao.DadosInsertApuracoesRcd;
import codiub.competicoes.api.DTO.apuracao.DadosListApuracaoAndInscricaoRcd;
import codiub.competicoes.api.DTO.apuracao.DadosListApuracoesRcd;
import codiub.competicoes.api.DTO.apuracao.DadosUpdateApuracoesRcd;
import codiub.competicoes.api.DTO.atletas.DadosAtletasReduzidoRcd;
import codiub.competicoes.api.DTO.pessoas.pessoasfj.DadosPessoasfjReduzRcd;
import codiub.competicoes.api.client.PessoaApiClient;
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
import java.util.Set;

@RestController
@RequestMapping("/apuracao")
public class ApuracaoController {
    @Autowired
    private ApuracaoRepository apuracaoRepository;
    @Autowired
    private ApuracaoService apuracaoService;
    @Autowired private PessoaApiClient pessoaApiClient;

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
    public ResponseEntity<DadosListApuracoesRcd> update(@PathVariable @Valid Long id, @RequestBody DadosUpdateApuracoesRcd dados) {
        // 1. O service retorna a entidade 'Apuracao' atualizada.
        Apuracao apuracaoSalva = apuracaoService.update(id, dados);

        // 2. Buscamos o nome do atleta para enriquecer a resposta.
        Long pessoaId = apuracaoSalva.getAtleta().getPessoaId();
        List<DadosPessoasfjReduzRcd> pessoaInfoList = pessoaApiClient.findPessoasByIds(Set.of(pessoaId));

        String atletaNome = "Nome não encontrado";
        if (pessoaInfoList != null && !pessoaInfoList.isEmpty()) {
            atletaNome = pessoaInfoList.get(0).nome();
        }

        // 3. Criamos o DTO de resposta com os dois argumentos necessários.
        DadosListApuracoesRcd respostaDTO = DadosListApuracoesRcd.fromApuracao(apuracaoSalva, atletaNome);

        // 4. Retornamos a resposta 200 OK com o corpo completo.
        return ResponseEntity.ok(respostaDTO);
    }

    //INSERT
    @PostMapping
    @Transactional
    public ResponseEntity<DadosListApuracoesRcd> insert(@RequestBody @Valid DadosInsertApuracoesRcd dados) {
        // 1. O service retorna a entidade 'Apuracao' recém-criada.
        Apuracao apuracaoSalva = apuracaoService.insert(dados);

        // 2. Construímos a URI para o cabeçalho 'Location'.
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(apuracaoSalva.getId()).toUri();

        // 3. Buscamos o nome do atleta para enriquecer a resposta.
        Long pessoaId = apuracaoSalva.getAtleta().getPessoaId();
        List<DadosPessoasfjReduzRcd> pessoaInfoList = pessoaApiClient.findPessoasByIds(Set.of(pessoaId));

        String atletaNome = "Nome não encontrado";
        if (pessoaInfoList != null && !pessoaInfoList.isEmpty()) {
            atletaNome = pessoaInfoList.get(0).nome();
        }

        // 4. Criamos o DTO de resposta usando o método de fábrica com os dois parâmetros.
        DadosListApuracoesRcd respostaDTO = DadosListApuracoesRcd.fromApuracao(apuracaoSalva, atletaNome);

        // 5. Retornamos a resposta 201 Created com a URI e o corpo completo.
        return ResponseEntity.created(uri).body(respostaDTO);
    }

    // DELETAR
    @Transactional
    @DeleteMapping(value = "/{id}")
    public ResponseEntity delete(@PathVariable Long id){
        apuracaoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}


