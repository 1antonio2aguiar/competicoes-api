package codiub.competicoes.api.repository;

import codiub.competicoes.api.entity.TiposVeiculos;
import codiub.competicoes.api.repository.tiposVeiculos.TiposVeiculosRepositoryQuery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

public interface TiposVeiculosRepository extends JpaRepository<TiposVeiculos, Long>, TiposVeiculosRepositoryQuery {

}
