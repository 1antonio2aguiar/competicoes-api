package codiub.competicoes.api.repository.pessoas.pessoaFisica;

import codiub.competicoes.api.entity.pessoas.pessoa.PessoaFisica;
import codiub.competicoes.api.filter.pessoas.PessoaFisicaFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PessoaFisicaRepositoryQuery {
    public Page<PessoaFisica> filtrar(PessoaFisicaFilter filter, Pageable pageable);

    public List<PessoaFisica> filtrar(PessoaFisicaFilter Filter);
}
