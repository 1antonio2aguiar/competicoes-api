package codiub.competicoes.api.repository;

import codiub.competicoes.api.entity.Parametro;
import codiub.competicoes.api.entity.Prova;
import codiub.competicoes.api.repository.parametro.ParametroRepositoryQuery;
import codiub.competicoes.api.repository.prova.ProvaRepositoryQuery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParametroRepository extends JpaRepository<Parametro, Long>, ParametroRepositoryQuery {

}
