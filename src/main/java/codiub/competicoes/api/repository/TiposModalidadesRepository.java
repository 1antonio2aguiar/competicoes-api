package codiub.competicoes.api.repository;

import codiub.competicoes.api.entity.TiposModalidades;
import codiub.competicoes.api.entity.TiposVeiculos;
import codiub.competicoes.api.repository.tiposModalidades.TiposModalidadesRepositoryQuery;
import codiub.competicoes.api.repository.tiposVeiculos.TiposVeiculosRepositoryQuery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TiposModalidadesRepository extends JpaRepository<TiposModalidades, Long>, TiposModalidadesRepositoryQuery {

}
