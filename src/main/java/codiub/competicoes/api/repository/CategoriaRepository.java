package codiub.competicoes.api.repository;

import codiub.competicoes.api.entity.Categoria;
import codiub.competicoes.api.repository.campeonato.CampeonatoRepositoryQuery;
import codiub.competicoes.api.repository.categoria.CategoriaRepositoryQuery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long>, CategoriaRepositoryQuery {

}
