package codiub.competicoes.api.repository.seguranca.perfil;

import codiub.competicoes.api.entity.seguranca.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
public interface PerfilRepository extends JpaRepository<Perfil, Long>, PerfilRepositoryQuery {
    Optional<Perfil> findByNome(String nome);
}