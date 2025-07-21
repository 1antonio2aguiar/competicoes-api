package codiub.competicoes.api.repository.tiposNados;

import codiub.competicoes.api.entity.TipoNado;
import codiub.competicoes.api.filter.TipoNadoFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TipoNadoRepositoryQuery {
    public Page<TipoNado> filtrar(TipoNadoFilter tipoNadoFilter, Pageable pageable);

    public List<TipoNado> filtrar(TipoNadoFilter Filter);
}
