package codiub.competicoes.api.repository.pontuacao;

import codiub.competicoes.api.entity.Pontuacao;
import codiub.competicoes.api.filter.PontuacaoFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PontuacaoRepositoryQuery {
    public Page<Pontuacao> filtrar(PontuacaoFilter pontuacaoFilter, Pageable pageable);

    public List<Pontuacao> filtrar(PontuacaoFilter Filter);
}
