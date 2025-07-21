package codiub.competicoes.api.repository.pessoas.pessoas;

import codiub.competicoes.api.entity.pessoas.Pessoas;
import codiub.competicoes.api.filter.pessoas.PessoasFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PessoasRepositoryQuery {
    public Page<Pessoas> filtrar(PessoasFilter pessoasFilter, Pageable pageable);

    public List<Pessoas> filtrar(PessoasFilter Filter);
}
