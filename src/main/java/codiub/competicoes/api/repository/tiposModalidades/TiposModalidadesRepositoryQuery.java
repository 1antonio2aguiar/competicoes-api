package codiub.competicoes.api.repository.tiposModalidades;

import codiub.competicoes.api.entity.TiposModalidades;
import codiub.competicoes.api.filter.TiposModalidadesFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TiposModalidadesRepositoryQuery {
    public Page<TiposModalidades> filtrar(TiposModalidadesFilter tiposModalidadesFilter, Pageable pageable);

    public List<TiposModalidades> filtrar(TiposModalidadesFilter filter);
}
