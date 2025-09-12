package codiub.competicoes.api.commom;

import codiub.competicoes.api.entity.seguranca.Usuario;
import codiub.competicoes.api.repository.seguranca.usuario.UsuarioRepository;
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
            var subject = tokenService.getSubject(tokenJWT); // Isso é o email
            UserDetails usuario = usuarioRepository.findByEmail(subject); // Busca o usuário completo

            if (usuario != null) {
                // É bom fazer um cast aqui para a sua classe Usuario
                Usuario usuarioAutenticado = (Usuario) usuario;

                // Agora, criamos o token de autenticação para o Spring Security
                // Podemos incluir o empresaId aqui de várias formas.
                // Uma maneira simples é adicionar como um "detail" extra.
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(usuarioAutenticado, null, usuarioAutenticado.getAuthorities());

                // Opcional: Adicionar o empresaId diretamente nos detalhes da autenticação
                // Isso permite recuperar com SecurityContextHolder.getContext().getAuthentication().getDetails()
                if (usuarioAutenticado.getEmpresa() != null) {
                    authentication.setDetails(usuarioAutenticado.getEmpresa().getId());
                } else {
                    // Tratar caso o usuário não tenha empresa (log ou erro)
                    System.err.println("Usuário " + usuarioAutenticado.getEmail() + " não possui empresa associada no SecurityFilter.");
                }


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