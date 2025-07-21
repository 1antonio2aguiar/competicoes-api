package codiub.competicoes.api.repository.equipe;

import codiub.competicoes.api.entity.Equipe;
import codiub.competicoes.api.filter.EquipeFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipeRepositoryQuery {
    public Page<Equipe> filtrar(EquipeFilter equipeFilter, Pageable pageable);

    public List<Equipe> filtrar(EquipeFilter Filter);
}
