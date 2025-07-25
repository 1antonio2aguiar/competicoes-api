package codiub.competicoes.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients   // <<<< ADICIONAR ESTA ANOTAÇÃO PARA TRABALHAR COM ORQUESTRAÇÃO NO CASO AQUIU  PESSOAS-API
public class ApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}

}
