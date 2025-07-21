package codiub.competicoes.api.repository.pessoas;

import codiub.competicoes.api.entity.pessoas.pessoa.PessoaJuridica;
import codiub.competicoes.api.repository.pessoas.pessoaJuridica.PessoaJuridicaRepositoryQuery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PessoaJuridicaRepository extends JpaRepository<PessoaJuridica, Long>, PessoaJuridicaRepositoryQuery {

}
