package codiub.competicoes.api.repository.seguranca.usuario;

import codiub.competicoes.api.entity.seguranca.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>, UsuarioRepositoryQuery {
    // Método que o Spring Security vai usar para buscar um usuário pelo email
    UserDetails findByEmail(String email);

    boolean existsByEmail(String email);
}