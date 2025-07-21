package codiub.competicoes.api.repository;

import codiub.competicoes.api.entity.LocaisCompeticoes;
import codiub.competicoes.api.repository.locaisCompeticoes.LocaisCompeticoesRepositoryQuery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocaisCompeticoesRepository extends JpaRepository<LocaisCompeticoes, Long>, LocaisCompeticoesRepositoryQuery {

}
