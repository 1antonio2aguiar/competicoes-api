package codiub.competicoes.api.repository;

import codiub.competicoes.api.entity.Campeonato;
import codiub.competicoes.api.entity.TiposModalidades;
import codiub.competicoes.api.filter.CampeonatoFilter;
import codiub.competicoes.api.filter.TiposModalidadesFilter;
import codiub.competicoes.api.repository.campeonato.CampeonatoRepositoryQuery;
import codiub.competicoes.api.repository.tiposModalidades.TiposModalidadesRepositoryQuery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CampeonatoRepository extends JpaRepository<Campeonato, Long>, CampeonatoRepositoryQuery {

}
