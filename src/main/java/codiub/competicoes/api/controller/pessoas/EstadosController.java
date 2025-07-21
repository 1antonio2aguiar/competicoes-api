package codiub.competicoes.api.controller.pessoas;

import codiub.competicoes.api.DTO.pessoas.DadosEstadosRcd;
import codiub.competicoes.api.repository.pessoas.EstadosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/estados")
public class EstadosController {
    @Autowired private EstadosRepository estadosRepository;

    // Listar estados
    @GetMapping
    public Page<DadosEstadosRcd> findall(@PageableDefault (sort={"nome"}) Pageable paginacao){
        return estadosRepository.findAll(paginacao).map(DadosEstadosRcd::new);
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

