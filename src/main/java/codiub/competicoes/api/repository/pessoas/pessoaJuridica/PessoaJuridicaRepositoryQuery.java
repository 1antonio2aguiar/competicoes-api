package codiub.competicoes.api.repository.pessoas.pessoaJuridica;

import codiub.competicoes.api.entity.pessoas.pessoa.PessoaFisica;
import codiub.competicoes.api.entity.pessoas.pessoa.PessoaJuridica;
import codiub.competicoes.api.filter.pessoas.PessoaFisicaFilter;
import codiub.competicoes.api.filter.pessoas.PessoaJuridicaFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PessoaJuridicaRepositoryQuery {
    public Page<PessoaJuridica> filtrar(PessoaJuridicaFilter filter, Pageable pageable);

    public List<PessoaJuridica> filtrar(PessoaJuridicaFilter Filter);
}
