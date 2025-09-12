package codiub.competicoes.api.commom;

import codiub.competicoes.api.repository.seguranca.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService implements UserDetailsService {

    @Autowired
    private UsuarioRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // A nossa lógica é simples: buscar o usuário pelo email no repositório.
        UserDetails user = repository.findByEmail(username);

        if (user == null) {
            throw new UsernameNotFoundException("Usuário não encontrado com o email: " + username);
        }

        return user;
    }
}
