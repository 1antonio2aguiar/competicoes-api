package codiub.competicoes.api.repository.pessoas;

import codiub.competicoes.api.entity.pessoas.Pessoas;
import codiub.competicoes.api.repository.pessoas.paises.PaisesRepositoryQuery;
import codiub.competicoes.api.repository.pessoas.pessoas.PessoasRepositoryQuery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PessoasRepository extends JpaRepository<Pessoas, Long>, PessoasRepositoryQuery {

}
