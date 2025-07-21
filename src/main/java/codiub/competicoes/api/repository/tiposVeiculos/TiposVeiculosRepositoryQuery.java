package codiub.competicoes.api.repository.tiposVeiculos;

import codiub.competicoes.api.entity.TiposVeiculos;
import codiub.competicoes.api.filter.TiposVeiculosFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
@Repository
public interface TiposVeiculosRepositoryQuery {
    public Page<TiposVeiculos> filtrar(TiposVeiculosFilter tiposVeiculosFilter, Pageable pageable);
}
