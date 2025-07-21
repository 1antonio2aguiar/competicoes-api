package codiub.competicoes.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder; // Para mais opções de configuração

@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        // Você pode configurar o RestTemplate aqui (ex: timeouts, interceptors)
        return builder.build();
    }
}
