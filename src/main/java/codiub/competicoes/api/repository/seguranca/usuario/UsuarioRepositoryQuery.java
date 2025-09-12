package codiub.competicoes.api.repository.seguranca.usuario;

import codiub.competicoes.api.entity.Empresa;
import codiub.competicoes.api.entity.seguranca.Usuario;
import codiub.competicoes.api.filter.EmpresaFilter;
import codiub.competicoes.api.filter.UsuarioFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRepositoryQuery {
    public Page<Usuario> filtrar(UsuarioFilter filter, Pageable pageable);

    public List<Usuario> filtrar(UsuarioFilter Filter);
}
