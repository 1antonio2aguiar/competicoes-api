package codiub.competicoes.api.repository.pessoas;

import codiub.competicoes.api.entity.pessoas.Estados;
import codiub.competicoes.api.repository.pessoas.estados.EstadosRepositoryQuery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstadosRepository extends JpaRepository<Estados, Long>, EstadosRepositoryQuery {

}
