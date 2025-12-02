package codiub.competicoes.api.commom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.web.cors.CorsConfiguration; // Importe esta classe
import org.springframework.web.cors.CorsConfigurationSource; // Importe esta classe
import org.springframework.web.cors.UrlBasedCorsConfigurationSource; // Importe esta classe

import java.util.Arrays; // Importe esta classe
import java.util.List; // Importe esta classe

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

    @Autowired
    private SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .cors(withDefaults()) // Mantemos isso, mas ele usará o CorsConfigurationSource que vamos definir
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(req -> {
                    //req.requestMatchers(HttpMethod.POST, "/auth/login").permitAll();
                    //req.requestMatchers(HttpMethod.POST, "/usuarios").permitAll();
                    //req.anyRequest().authenticated();

                    req.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll();
                    req.requestMatchers(HttpMethod.POST, "/auth/login", "/usuarios").permitAll();
                    req.anyRequest().authenticated();
                })
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ======================================================================
    // <<< NOVO BEAN PARA CONFIGURAR O CORS ESPECIFICAMENTE PARA O FRONTEND
    // ======================================================================
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:4200",
                "http://localhost:8081",
                "http://competicoes-frontend:8080"
        ));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Requested-With", "Accept"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    // ======================================================================
}