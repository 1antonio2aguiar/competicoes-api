package codiub.competicoes.api.service.client;

import codiub.competicoes.api.DTO.pessoas.pessoa.PessoaApiRequest;
import codiub.competicoes.api.DTO.pessoas.pessoa.PessoaApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class PessoaRemoteClientService {

    @Autowired
    private RestTemplate restTemplate;

    // Configure a URL base da sua pessoas-api no application.properties do projeto competicoes
    @Value("${api.pessoas.baseurl:http://localhost:8081/pessoaFisica}")
    private String pessoasApiBaseUrl;

    /**
     * Insere uma PessoaFisica chamando a pessoas-api.
     *
     * @param dados Os dados para criar a pessoa, formatados como a pessoas-api espera.
     * @return O DTO da pessoa criada, retornado pela pessoas-api.
     * @throws RuntimeException se a chamada à API falhar.
     */
    public PessoaApiResponse inserirPessoaFisica(PessoaApiRequest dadosParaApi) { // << Parâmetro usa DTO de request da API
        try {
            ResponseEntity<PessoaApiResponse> response = restTemplate.postForEntity(
                    pessoasApiBaseUrl,
                    dadosParaApi,
                    PessoaApiResponse.class // << Resposta usa DTO de response da API
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                System.out.println("Pessoa inserida remotamente com sucesso: " + response.getBody());
                return response.getBody();
            } else {
                System.err.println("Erro ao inserir pessoa remotamente. Status: " + response.getStatusCode() +
                        " Corpo: " + response.getBody());
                throw new RuntimeException("Falha ao inserir pessoa remotamente. Status: " + response.getStatusCode());
            }
        } catch (HttpClientErrorException e) {
            System.err.println("Erro HTTP ao chamar API de pessoas (insert): " + e.getStatusCode() +
                    " - " + e.getResponseBodyAsString());
            throw new RuntimeException("Erro HTTP ao comunicar com a API de pessoas: " + e.getMessage(), e);
        } catch (Exception e) {
            System.err.println("Erro inesperado ao chamar API de pessoas (insert): " + e.getMessage());
            throw new RuntimeException("Erro inesperado ao comunicar com a API de pessoas", e);
        }
    }

    public PessoaApiResponse buscarPessoaFisicaPorId(Long id) {
        //System.err.println("É AQUI QUE ESTA VINDO>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        try {
            String url = pessoasApiBaseUrl + "/" + id;
            ResponseEntity<PessoaApiResponse> response = restTemplate.getForEntity(url, PessoaApiResponse.class); // << Resposta usa DTO

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody();
            } else if (response.getStatusCode().value() == 404) {
                System.out.println("Pessoa não encontrada remotamente (API) com ID: " + id);
                return null;
            } else {
                System.err.println("Erro ao buscar pessoa remotamente (API). Status: " + response.getStatusCode());
                throw new RuntimeException("Falha ao buscar pessoa remotamente (API). Status: " + response.getStatusCode());
            }
        } catch (HttpClientErrorException.NotFound e) {
            System.out.println("Pessoa não encontrada remotamente (API - 404) com ID: " + id);
            return null;
        } catch (Exception e) {
            System.err.println("Erro ao buscar pessoa por ID na API remota: " + e.getMessage());
            throw new RuntimeException("Erro ao comunicar com a API de pessoas para buscar por ID", e);
        }
    }

    // Adicione métodos para UPDATE, DELETE, LISTAGEM conforme necessário
}
