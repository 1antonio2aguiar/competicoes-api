package codiub.competicoes.api.controller;

import codiub.competicoes.api.DTO.equipe.DadosInsertEquipeRcd;
import codiub.competicoes.api.DTO.equipe.DadosListEquipeRcd;
import codiub.competicoes.api.DTO.equipe.DadosUpdateEquipeRcd;
import codiub.competicoes.api.DTO.etapa.DadosInsertEtapaRcd;
import codiub.competicoes.api.DTO.etapa.DadosListEtapaRcd;
import codiub.competicoes.api.DTO.pessoas.pessoasfj.DadosPessoasfjReduzRcd;
import codiub.competicoes.api.client.PessoaApiClient;
import org.springframework.transaction.annotation.Transactional;
import codiub.competicoes.api.entity.Equipe;
import codiub.competicoes.api.filter.EquipeFilter;
import codiub.competicoes.api.repository.EquipeRepository;
import codiub.competicoes.api.service.EquipeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/equipe")
public class EquipeController {
    @Autowired
    private EquipeRepository equipeRepository;
    @Autowired
    private EquipeService equipeService;
    @Autowired
    private PessoaApiClient pessoaApiClient;


    // Listar equipes
    @GetMapping
    public Page<DadosListEquipeRcd> findall(@PageableDefault(sort = {"nome"}) Pageable paginacao) {
        return equipeService.findall(paginacao);
    }

    @GetMapping("/list")
    public List<Equipe> pesquisar(EquipeFilter filter ) {
        return equipeRepository.filtrar(filter);
    }

    @GetMapping("/filter")
    public Page<DadosListEquipeRcd> pesquisar(EquipeFilter filter, Pageable pageable) {
        return equipeService.pesquisar(filter, pageable);
    }
    @GetMapping("/{id}")
    public ResponseEntity<DadosListEquipeRcd> findById(@PathVariable Long id) {
        DadosListEquipeRcd dadosEquipeRcd = equipeService.findById(id);
        return dadosEquipeRcd != null
                ? ResponseEntity.ok(dadosEquipeRcd)
                : ResponseEntity.notFound().build();
    }

    // ALTERAR
    @Transactional
    @PutMapping("/{id}")
    public ResponseEntity<DadosListEquipeRcd> update(@PathVariable @Valid Long id, @RequestBody DadosUpdateEquipeRcd dados) {
        // 1. O serviço salva a entidade e retorna a Equipe atualizada com os IDs corretos.
        Equipe equipeSalva = equipeService.update(id, dados);

        // 2. Coleta todos os IDs de pessoas da equipe salva.
        Set<Long> pessoaIds = new HashSet<>();
        if (equipeSalva.getAgremiacaoId() != null) pessoaIds.add(equipeSalva.getAgremiacaoId());
        if (equipeSalva.getTecnicoId() != null) pessoaIds.add(equipeSalva.getTecnicoId());
        if (equipeSalva.getAssistenteTecnicoId() != null) pessoaIds.add(equipeSalva.getAssistenteTecnicoId());

        // 3. Faz uma ÚNICA chamada à API de pessoas para buscar todos os dados necessários.
        // Se não houver IDs, retorna uma lista vazia para evitar chamadas desnecessárias.
        List<DadosPessoasfjReduzRcd> pessoasDtos = !pessoaIds.isEmpty()
                ? pessoaApiClient.findPessoasByIds(pessoaIds)
                : Collections.emptyList();

        // 4. Cria um mapa para acesso rápido às pessoas por ID, facilitando a construção do DTO.
        Map<Long, DadosPessoasfjReduzRcd> pessoasMap = pessoasDtos.stream()
                .collect(Collectors.toMap(DadosPessoasfjReduzRcd::id, Function.identity()));

        // 5. Usa os dados obtidos para construir o DTO de resposta.
        DadosListEquipeRcd dtoResposta = new DadosListEquipeRcd(
                equipeSalva.getId(),
                equipeSalva.getEmpresa() != null ? equipeSalva.getEmpresa().getNome() : null,
                pessoasMap.get(equipeSalva.getAgremiacaoId()), // Busca a agregação no mapa
                equipeSalva.getNome(),
                equipeSalva.getSigla(),
                equipeSalva.getModalidade() != null ? equipeSalva.getModalidade().getId() : null,
                equipeSalva.getModalidade() != null ? equipeSalva.getModalidade().getNome() : null,
                pessoasMap.get(equipeSalva.getTecnicoId()), // Busca o técnico no mapa
                pessoasMap.get(equipeSalva.getAssistenteTecnicoId()) // Busca o assistente no mapa
        );

        // 6. Retorna o DTO completo na resposta.
        return ResponseEntity.ok(dtoResposta);
    }

    //INSERT
    @PostMapping
    @Transactional
    public ResponseEntity<DadosListEquipeRcd> insert(@RequestBody @Valid DadosInsertEquipeRcd dados) {
        // 1. O serviço salva a entidade e retorna a Equipe com os IDs corretos.
        Equipe equipeSalva = equipeService.insert(dados);

        // 2. Coleta todos os IDs de pessoas da equipe recém-salva.
        Set<Long> pessoaIds = new HashSet<>();
        if (equipeSalva.getAgremiacaoId() != null) pessoaIds.add(equipeSalva.getAgremiacaoId());
        if (equipeSalva.getTecnicoId() != null) pessoaIds.add(equipeSalva.getTecnicoId());
        if (equipeSalva.getAssistenteTecnicoId() != null) pessoaIds.add(equipeSalva.getAssistenteTecnicoId());

        // 3. Faz uma ÚNICA chamada à API de pessoas para buscar os dados.
        List<DadosPessoasfjReduzRcd> pessoasDtos = !pessoaIds.isEmpty()
                ? pessoaApiClient.findPessoasByIds(pessoaIds)
                : Collections.emptyList();

        // 4. Cria um mapa para acesso rápido às pessoas por ID.
        Map<Long, DadosPessoasfjReduzRcd> pessoasMap = pessoasDtos.stream()
                .collect(Collectors.toMap(DadosPessoasfjReduzRcd::id, Function.identity()));

        // 5. Usa o método de fábrica para construir o DTO de resposta completo.
        DadosListEquipeRcd dtoResposta = DadosListEquipeRcd.fromEntityAndMap(equipeSalva, pessoasMap);

        // 6. Cria a URI para o cabeçalho "Location" da resposta 201 Created.
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}") // CORRIGIDO: use {id}
                .buildAndExpand(equipeSalva.getId()).toUri();

        // 7. Retorna a resposta 201 Created com a URI e o corpo contendo o DTO completo.
        return ResponseEntity.created(uri).body(dtoResposta);
    }

    // DELETAR
    @Transactional
    @DeleteMapping(value = "/{id}")
    public ResponseEntity delete(@PathVariable Long id){
        equipeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}


