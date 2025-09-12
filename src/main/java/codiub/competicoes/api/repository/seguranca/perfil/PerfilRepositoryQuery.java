package codiub.competicoes.api.repository.seguranca.perfil;

import codiub.competicoes.api.entity.seguranca.Perfil;
import codiub.competicoes.api.entity.seguranca.Usuario;
import codiub.competicoes.api.filter.PerfilFilter;
import codiub.competicoes.api.filter.UsuarioFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PerfilRepositoryQuery {
    Page<Perfil> filtrar(PerfilFilter filter, Pageable pageable);
    List<Perfil> filtrar(PerfilFilter filter);
}
