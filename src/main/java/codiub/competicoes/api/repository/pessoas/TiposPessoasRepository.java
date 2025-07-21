package codiub.competicoes.api.repository.pessoas;

import codiub.competicoes.api.entity.pessoas.Paises;
import codiub.competicoes.api.entity.pessoas.TiposPessoas;
import codiub.competicoes.api.repository.pessoas.paises.PaisesRepositoryQuery;
import codiub.competicoes.api.repository.pessoas.tiposPessoas.TiposPessoasRepositoryQuery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TiposPessoasRepository extends JpaRepository<TiposPessoas, Long>, TiposPessoasRepositoryQuery {

}
