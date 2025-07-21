package codiub.competicoes.api.repository.campeonato;

import codiub.competicoes.api.entity.Campeonato;
import codiub.competicoes.api.filter.CampeonatoFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CampeonatoRepositoryQuery {
    public Page<Campeonato> filtrar(CampeonatoFilter campeonatoFilter, Pageable pageable);

    public List<Campeonato> filtrar(CampeonatoFilter Filter);
}
