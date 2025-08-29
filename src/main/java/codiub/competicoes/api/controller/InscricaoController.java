package codiub.competicoes.api.controller;

import codiub.competicoes.api.DTO.inscricoes.DadosInsertInscricoesRcd;
import codiub.competicoes.api.DTO.inscricoes.DadosListInscricoesRcd;
import codiub.competicoes.api.DTO.inscricoes.DadosUpdateInscricoesRcd;
import codiub.competicoes.api.DTO.pessoas.pessoasfj.DadosPessoasfjReduzRcd;
import codiub.competicoes.api.client.PessoaApiClient;
import codiub.competicoes.api.entity.Inscricoes;
import codiub.competicoes.api.filter.InscricaoFilter;
import codiub.competicoes.api.repository.InscricaoRepository;
import codiub.competicoes.api.service.InscricaoService;
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
@RequestMapping("/inscricao")
public class InscricaoController {
    @Autowired
    private InscricaoRepository inscricaoRepository;
    @Autowired
    private InscricaoService inscricaoService;
    @Autowired private PessoaApiClient pessoaApiClient;

    // Listar inscrições
    @GetMapping
    public Page<DadosListInscricoesRcd> findall(@PageableDefault(sort = {"id"}) Pageable paginacao) {
        return inscricaoService.findall(paginacao);
    }

    @GetMapping("/list")
    public List<Inscricoes> pesquisar(InscricaoFilter filter ) {
        return inscricaoRepository.filtrar(filter);
    }

    @GetMapping("/filter")
    public Page<DadosListInscricoesRcd> pesquisar(InscricaoFilter filter, Pageable pageable) {
        return inscricaoService.pesquisar(filter, pageable);
    }
    @GetMapping("/{id}")
    public ResponseEntity<DadosListInscricoesRcd> findById(@PathVariable Long id) {
        DadosListInscricoesRcd dados = inscricaoService.findById(id);
        return dados != null
                ? ResponseEntity.ok(dados)
                : ResponseEntity.notFound().build();
    }

    // ALTERAR
    @Transactional
    @PutMapping(value = "/{id}")
    public ResponseEntity<DadosListInscricoesRcd> update(@PathVariable @Valid Long id, @RequestBody DadosUpdateInscricoesRcd dados) {
        // 1. O método update do service retorna a entidade 'Inscricoes' atualizada.
        Inscricoes inscricaoSalva = inscricaoService.update(id, dados);

        // 2. Precisamos buscar o nome do atleta para construir a resposta.
        Long pessoaId = inscricaoSalva.getAtleta().getPessoaId();
        List<DadosPessoasfjReduzRcd> pessoaInfoList = pessoaApiClient.findPessoasByIds(Set.of(pessoaId));

        String atletaNome = "Nome não encontrado";
        if (pessoaInfoList != null && !pessoaInfoList.isEmpty()) {
            atletaNome = pessoaInfoList.get(0).nome();
        }

        // 3. Agora, chamamos o método de fábrica com os dois argumentos necessários.
        DadosListInscricoesRcd respostaDTO = DadosListInscricoesRcd.fromInscricao(inscricaoSalva, atletaNome);

        return ResponseEntity.ok(respostaDTO);
    }

    //INSERT
    @PostMapping
    @Transactional
    public ResponseEntity<DadosListInscricoesRcd> insert(@RequestBody @Valid DadosInsertInscricoesRcd dados) {
        // 1. O método insert do service retorna a entidade 'Inscricoes' recém-criada.
        Inscricoes inscricaoSalva = inscricaoService.insert(dados);

        // 2. Construímos a URI para o cabeçalho 'Location', como antes.
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}") // Corrigido para a sintaxe correta
                .buildAndExpand(inscricaoSalva.getId()).toUri();

        // 3. Buscamos o nome do atleta para enriquecer o corpo da resposta.
        Long pessoaId = inscricaoSalva.getAtleta().getPessoaId();
        List<DadosPessoasfjReduzRcd> pessoaInfoList = pessoaApiClient.findPessoasByIds(Set.of(pessoaId));

        String atletaNome = "Nome não encontrado";
        if (pessoaInfoList != null && !pessoaInfoList.isEmpty()) {
            atletaNome = pessoaInfoList.get(0).nome();
        }

        // 4. Criamos o DTO de resposta com a inscrição e o nome do atleta.
        DadosListInscricoesRcd respostaDTO = DadosListInscricoesRcd.fromInscricao(inscricaoSalva, atletaNome);

        // 5. Retornamos a resposta 201 Created com a URI e o corpo completo.
        return ResponseEntity.created(uri).body(respostaDTO);
    }

    // DELETAR
    @Transactional
    @DeleteMapping(value = "/{id}")
    public ResponseEntity delete(@PathVariable Long id){
        inscricaoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}


