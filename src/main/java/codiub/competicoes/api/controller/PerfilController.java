package codiub.competicoes.api.controller;

import codiub.competicoes.api.DTO.campeonato.DadosInsertCampeonatoRcd;
import codiub.competicoes.api.DTO.campeonato.DadosListCampeonatoRcd;
import codiub.competicoes.api.DTO.campeonato.DadosUpdateCampeonatoRcd;
import codiub.competicoes.api.DTO.perfis.DadosListPerfil;
import codiub.competicoes.api.entity.Campeonato;
import codiub.competicoes.api.entity.seguranca.Perfil;
import codiub.competicoes.api.filter.CampeonatoFilter;
import codiub.competicoes.api.filter.PerfilFilter;
import codiub.competicoes.api.repository.CampeonatoRepository;
import codiub.competicoes.api.repository.seguranca.perfil.PerfilRepository;
import codiub.competicoes.api.service.CampeonatoService;
import codiub.competicoes.api.service.PerfilService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/perfis")
public class PerfilController {
    @Autowired private PerfilService perfilService;
    @Autowired private CampeonatoRepository campeonatoRepository;
    @Autowired
    private PerfilRepository perfilRepository;

    // Listar campeonatos
    @GetMapping
    public Page<DadosListPerfil> findall(@PageableDefault (sort={"nome"}) Pageable paginacao){
        return perfilRepository.findAll(paginacao).map(DadosListPerfil::new);
    }

    // Lista de tipos veiculos com Paginacao e filter
    @GetMapping("/filter")
    public Page<Perfil> pesquisar(PerfilFilter filter, Pageable pageable) {
        return perfilRepository.filtrar(filter, pageable);
    }

    @GetMapping("/list")
    public List<Perfil> pesquisar(PerfilFilter filter ) {
        return perfilRepository.filtrar(filter);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosListPerfil> findById(@PathVariable Long id) {
        Perfil perfil = perfilService.findById(id);
        return ResponseEntity.ok(new DadosListPerfil(perfil));
    }

    // Inserir
    @PostMapping
    @Transactional
    public ResponseEntity<DadosListPerfil> insert(@RequestBody @Valid DadosListPerfil dados, UriComponentsBuilder uriBuilder) {
        Perfil novoPerfil = perfilService.insert(dados);
        URI uri = uriBuilder.path("/perfis/{id}").buildAndExpand(novoPerfil.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosListPerfil(novoPerfil));
    }

    // ALTERAR
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<DadosListPerfil> update(@PathVariable Long id, @RequestBody @Valid DadosListPerfil dados) {
        Perfil perfilAtualizado = perfilService.update(id, dados);
        return ResponseEntity.ok(new DadosListPerfil(perfilAtualizado));
    }

    // DELETAR
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        perfilService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

