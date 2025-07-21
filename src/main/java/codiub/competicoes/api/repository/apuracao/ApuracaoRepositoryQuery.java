package codiub.competicoes.api.repository.apuracao;

import codiub.competicoes.api.entity.Apuracao;
import codiub.competicoes.api.filter.ApuracaoFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApuracaoRepositoryQuery {
    public Page<Apuracao> filtrar(ApuracaoFilter apuracaoFilter, Pageable pageable);

    public List<Apuracao> filtrar(ApuracaoFilter Filter);

}
