package codiub.competicoes.api.repository;

import codiub.competicoes.api.entity.TipoNado;
import codiub.competicoes.api.repository.tiposNados.TipoNadoRepositoryQuery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TipoNadoRepository extends JpaRepository<TipoNado, Long>, TipoNadoRepositoryQuery {

}
