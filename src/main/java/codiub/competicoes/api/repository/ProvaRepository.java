package codiub.competicoes.api.repository;

import codiub.competicoes.api.entity.Prova;
import codiub.competicoes.api.repository.prova.ProvaRepositoryQuery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProvaRepository extends JpaRepository<Prova, Long>, ProvaRepositoryQuery {

}
