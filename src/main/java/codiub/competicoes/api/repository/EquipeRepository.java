package codiub.competicoes.api.repository;

import codiub.competicoes.api.entity.Equipe;
import codiub.competicoes.api.repository.equipe.EquipeRepositoryQuery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipeRepository extends JpaRepository<Equipe, Long>, EquipeRepositoryQuery {

}
