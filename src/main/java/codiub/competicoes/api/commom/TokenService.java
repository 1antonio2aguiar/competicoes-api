package codiub.competicoes.api.commom;

import codiub.competicoes.api.entity.seguranca.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    // Pega o valor da chave secreta do seu application.properties
    @Value("${api.security.token.secret}")
    private String secret;

    /**
     * Método que gera o Token JWT.
     * @param usuario O objeto do usuário que foi autenticado com sucesso.
     * @return Uma string com o token JWT.
     */
    public String gerarToken(Usuario usuario) {
        try {
            Algorithm algoritmo = Algorithm.HMAC256(secret);

            // ========================================================
            // <<< O FLUXO CORRETO COMEÇA AQUI
            // ========================================================
            return JWT.create() // 1. Começa a criação do token
                    .withIssuer("API Competicoes Codiub")      // 2. Adiciona o emissor
                    .withSubject(usuario.getEmail())         // 3. Adiciona o "dono" (email)
                    .withClaim("id", usuario.getId())        // 4. Adiciona o ID do usuário
                    .withClaim("nome", usuario.getNome())      // 5. <<< ADICIONA O NOME DO USUÁRIO
                    .withClaim("empresaId", usuario.getEmpresa().getId())
                    .withClaim("razaoSocial", usuario.getEmpresa().getRazaoSocial())
                    .withExpiresAt(dataExpiracao())          // 6. Adiciona a data de expiração
                    .sign(algoritmo);                        // 7. Assina e finaliza o token
            // ========================================================

        } catch (JWTCreationException exception){
            throw new RuntimeException("Erro ao gerar token JWT", exception);
        }
    }

    /**
     * Método que valida o token e retorna o "Subject" (usuário/email).
     * Será usado no SecurityFilter.
     * @param tokenJWT A string do token recebida no cabeçalho da requisição.
     * @return O email do usuário se o token for válido.
     */
    public String getSubject(String tokenJWT) {
        try {
            Algorithm algoritmo = Algorithm.HMAC256(secret);
            return JWT.require(algoritmo)
                    .withIssuer("API Competicoes Codiub") // Verifica se o emissor é o mesmo
                    .build()
                    .verify(tokenJWT) // Verifica a assinatura e a validade do token
                    .getSubject(); // Retorna o "subject" (email do usuário)
        } catch (JWTVerificationException exception) {
            // Lança uma exceção se o token for inválido, que será capturada pelo filtro de segurança
            throw new RuntimeException("Token JWT inválido ou expirado!");
        }
    }

    /**
     * Calcula o momento da expiração do token.
     * @return Um objeto Instant representando o momento da expiração (ex: 2 horas a partir de agora).
     */
    private Instant dataExpiracao() {
        // Define que o token expira em 2 horas, no fuso horário de Brasília
        return LocalDateTime.now().plusHours(12).toInstant(ZoneOffset.of("-03:00"));
    }

}