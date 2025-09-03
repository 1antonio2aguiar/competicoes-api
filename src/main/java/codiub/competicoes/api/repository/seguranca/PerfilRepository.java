package codiub.competicoes.api.repository.seguranca;

import codiub.competicoes.api.entity.seguranca.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PerfilRepository extends JpaRepository<Perfil, Long> {
}