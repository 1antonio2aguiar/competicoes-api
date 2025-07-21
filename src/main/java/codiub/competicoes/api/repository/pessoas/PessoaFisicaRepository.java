package codiub.competicoes.api.repository.pessoas;

import codiub.competicoes.api.entity.pessoas.pessoa.PessoaFisica;
import codiub.competicoes.api.repository.pessoas.pessoaFisica.PessoaFisicaRepositoryQuery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PessoaFisicaRepository extends JpaRepository<PessoaFisica, Long>, PessoaFisicaRepositoryQuery {

}
