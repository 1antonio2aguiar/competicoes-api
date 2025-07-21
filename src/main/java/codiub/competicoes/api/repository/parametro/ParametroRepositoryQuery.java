package codiub.competicoes.api.repository.parametro;

import codiub.competicoes.api.entity.Parametro;
import codiub.competicoes.api.entity.Prova;
import codiub.competicoes.api.filter.ParametroFilter;
import codiub.competicoes.api.filter.ProvaFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParametroRepositoryQuery {
    public Page<Parametro> filtrar(ParametroFilter parametroFilter, Pageable pageable);

    public List<Parametro> filtrar(ParametroFilter Filter);
}
