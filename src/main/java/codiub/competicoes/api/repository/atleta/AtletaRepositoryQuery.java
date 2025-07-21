package codiub.competicoes.api.repository.atleta;

import codiub.competicoes.api.entity.Atleta;
import codiub.competicoes.api.filter.AtletaFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AtletaRepositoryQuery {
    public Page<Atleta> filtrar(AtletaFilter atletaFilter, Pageable pageable);

    public List<Atleta> filtrar(AtletaFilter Filter);
}
