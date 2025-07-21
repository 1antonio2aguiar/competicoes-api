package codiub.competicoes.api.repository.etapa;

import codiub.competicoes.api.entity.Etapa;
import codiub.competicoes.api.filter.EtapaFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EtapaRepositoryQuery {
    public Page<Etapa> filtrar(EtapaFilter etapaFilter, Pageable pageable);
    public List<Etapa> filtrar(EtapaFilter Filter);
}
