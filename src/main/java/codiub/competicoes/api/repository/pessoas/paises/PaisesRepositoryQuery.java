package codiub.competicoes.api.repository.pessoas.paises;

import codiub.competicoes.api.entity.Campeonato;
import codiub.competicoes.api.entity.pessoas.Paises;
import codiub.competicoes.api.entity.pessoas.Pessoas;
import codiub.competicoes.api.filter.CampeonatoFilter;
import codiub.competicoes.api.filter.pessoas.PaisesFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaisesRepositoryQuery {
    public Page<Paises> filtrar(PaisesFilter paisesFilter, Pageable pageable);

    public List<Paises> filtrar(PaisesFilter Filter);
}
