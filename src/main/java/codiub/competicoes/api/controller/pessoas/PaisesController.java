package codiub.competicoes.api.controller.pessoas;

import codiub.competicoes.api.DTO.pessoas.DadosPaisesRcd;
import codiub.competicoes.api.repository.pessoas.PaisesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/paises")
public class PaisesController {
    @Autowired private PaisesRepository paisesRepository;

    // Listar paises
    @GetMapping
    public Page<DadosPaisesRcd> findall(@PageableDefault (sort={"nome"}) Pageable paginacao){
        return paisesRepository.findAll(paginacao).map(DadosPaisesRcd::new);
    }

    // Lista de tipos dados com Paginacao e filter
    /*@GetMapping("/filter")
    public Page<TipoNado> pesquisar(TipoNadoFilter filter, Pageable pageable) {
        return tipoNadoRepository.filtrar(filter, pageable);
    }*/

    /*@GetMapping(value = "/{id}")
    public ResponseEntity findById(@PathVariable Long id){
        TipoNado tipoNado = tipoNadoService.findById(id);
        return ResponseEntity.ok().body(new DadosTipoNadoRcd(tipoNado));
    }*/


}

