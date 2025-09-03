package codiub.competicoes.api.repository.seguranca;

import codiub.competicoes.api.entity.seguranca.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Método que o Spring Security vai usar para buscar um usuário pelo email
    UserDetails findByEmail(String email);

    boolean existsByEmail(String email);
}