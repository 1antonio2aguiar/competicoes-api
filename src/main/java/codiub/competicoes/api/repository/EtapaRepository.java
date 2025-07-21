package codiub.competicoes.api.repository;

import codiub.competicoes.api.entity.Etapa;
import codiub.competicoes.api.repository.etapa.EtapaRepositoryQuery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EtapaRepository extends JpaRepository<Etapa, Long>, EtapaRepositoryQuery {

}
