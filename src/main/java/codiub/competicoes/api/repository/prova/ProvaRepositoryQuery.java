package codiub.competicoes.api.repository.prova;

import codiub.competicoes.api.entity.Prova;
import codiub.competicoes.api.filter.ProvaFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProvaRepositoryQuery {
    public Page<Prova> filtrar(ProvaFilter provaFilter, Pageable pageable);

    public List<Prova> filtrar(ProvaFilter Filter);
}
