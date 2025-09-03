package codiub.competicoes.api.commom;

import codiub.competicoes.api.repository.seguranca.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component // Marca esta classe como um componente gerenciado pelo Spring
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 1. Recupera o token do cabeçalho da requisição
        var tokenJWT = recuperarToken(request);

        // 2. Se um token foi enviado...
        if (tokenJWT != null) {
            // 3. Valida o token e pega o email (subject) do usuário
            var subject = tokenService.getSubject(tokenJWT);

            // 4. Busca o usuário completo no banco de dados
            UserDetails usuario = usuarioRepository.findByEmail(subject);

            // 5. Se encontrou o usuário, informa ao Spring que ele está autenticado
            if (usuario != null) {
                // Cria um objeto de autenticação para o Spring
                var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());

                // Define a autenticação no contexto de segurança do Spring
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // 6. Continua o fluxo da requisição, permitindo que ela chegue ao controller
        filterChain.doFilter(request, response);
    }

    /**
     * Método auxiliar para extrair o token do cabeçalho "Authorization".
     */
    private String recuperarToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null) {
            // O token vem no formato "Bearer eyJhbGciOiJI..."
            // Precisamos remover o prefixo "Bearer "
            return authorizationHeader.replace("Bearer ", "").trim();
        }

        // Se não encontrou o cabeçalho, retorna nulo
        return null;
    }
}