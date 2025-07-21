package codiub.competicoes.api.controller;

import codiub.competicoes.api.DTO.parametro.DadosListParametroRcd;
import codiub.competicoes.api.DTO.provas.DadosInsertProvasRcd;
import codiub.competicoes.api.DTO.provas.DadosListProvasRcd;
import codiub.competicoes.api.DTO.provas.DadosUpdateProvasRcd;
import codiub.competicoes.api.entity.Prova;
import codiub.competicoes.api.filter.ParametroFilter;
import codiub.competicoes.api.filter.ProvaFilter;
import codiub.competicoes.api.repository.ProvaRepository;
import codiub.competicoes.api.service.ParametroService;
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
@RequestMapping("/parametro")
public class ParametroController {
    @Autowired
    private ParametroService parametroService;

    // Listar provas

    @GetMapping("/filter")
    public Page<DadosListParametroRcd> pesquisar(ParametroFilter filter, Pageable pageable) {
        return parametroService.pesquisar(filter, pageable);
    }

}


