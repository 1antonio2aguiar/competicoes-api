package codiub.competicoes.api.repository.locaisCompeticoes;
import codiub.competicoes.api.entity.LocaisCompeticoes;
import codiub.competicoes.api.filter.LocaisCompeticoesFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocaisCompeticoesRepositoryQuery {
    public Page<LocaisCompeticoes> filtrar(LocaisCompeticoesFilter locaisCompeticoesFilter, Pageable pageable);

    public List<LocaisCompeticoes> filtrar(LocaisCompeticoesFilter filter);
}
