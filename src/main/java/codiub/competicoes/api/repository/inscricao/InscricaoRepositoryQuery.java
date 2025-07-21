package codiub.competicoes.api.repository.inscricao;

import codiub.competicoes.api.entity.Inscricoes;
import codiub.competicoes.api.filter.InscricaoFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InscricaoRepositoryQuery {
    public Page<Inscricoes> filtrar(InscricaoFilter inscricaoFilter, Pageable pageable);

    public List<Inscricoes> filtrar(InscricaoFilter Filter);
}
