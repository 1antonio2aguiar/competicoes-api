package codiub.competicoes.api.repository;

import codiub.competicoes.api.entity.Atleta;
import codiub.competicoes.api.repository.atleta.AtletaRepositoryQuery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AtletaRepository extends JpaRepository<Atleta, Long>, AtletaRepositoryQuery {

}
