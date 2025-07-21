package codiub.competicoes.api.repository.pessoas;

import codiub.competicoes.api.entity.pessoas.Paises;
import codiub.competicoes.api.repository.pessoas.paises.PaisesRepositoryQuery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaisesRepository extends JpaRepository<Paises, Long>, PaisesRepositoryQuery {

}
