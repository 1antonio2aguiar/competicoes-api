package codiub.competicoes.api.repository.pessoas.estados;

import codiub.competicoes.api.entity.pessoas.Estados;
import codiub.competicoes.api.filter.pessoas.EstadosFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EstadosRepositoryQuery {
    public Page<Estados> filtrar(EstadosFilter estadosFilter, Pageable pageable);

    public List<Estados> filtrar(EstadosFilter Filter);
}
