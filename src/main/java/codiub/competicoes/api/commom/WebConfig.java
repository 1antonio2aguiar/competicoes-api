package codiub.competicoes.api.commom;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Aplica a configuração a TODOS os endpoints da sua API
            .allowedOrigins("http://localhost:4200") // Permite requisições vindas DESTE endereço
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "TRACE", "CONNECT") // Permite estes métodos HTTP
            .allowedHeaders("*") // Permite todos os cabeçalhos
            .allowCredentials(true); // Permite o envio de credenciais (cookies, etc.)
    }
}