package codiub.competicoes.api.repository.empresa;

import codiub.competicoes.api.entity.Categoria;
import codiub.competicoes.api.entity.Empresa;
import codiub.competicoes.api.filter.CategoriaFilter;
import codiub.competicoes.api.filter.EmpresaFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmpresaRepositoryQuery {
    public Page<Empresa> filtrar(EmpresaFilter filter, Pageable pageable);

    public List<Empresa> filtrar(EmpresaFilter Filter);
}
