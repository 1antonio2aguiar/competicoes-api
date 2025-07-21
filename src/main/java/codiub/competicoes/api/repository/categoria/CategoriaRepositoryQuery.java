package codiub.competicoes.api.repository.categoria;

import codiub.competicoes.api.entity.Campeonato;
import codiub.competicoes.api.entity.Categoria;
import codiub.competicoes.api.filter.CampeonatoFilter;
import codiub.competicoes.api.filter.CategoriaFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriaRepositoryQuery {
    public Page<Categoria> filtrar(CategoriaFilter categoriaFilter, Pageable pageable);

    public List<Categoria> filtrar(CategoriaFilter Filter);
}
