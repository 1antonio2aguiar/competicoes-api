package codiub.competicoes.api.client;

import codiub.competicoes.api.DTO.pessoas.DadosPessoasReduzidoRcd;
import codiub.competicoes.api.DTO.pessoas.pessoa.*;
import codiub.competicoes.api.DTO.pessoas.pessoasfj.DadosPessoasGeralRcd;
import codiub.competicoes.api.DTO.pessoas.pessoasfj.DadosPessoasfjReduzRcd;
import codiub.competicoes.api.entity.pessoas.pessoa.Pessoa;
import codiub.competicoes.api.utils.PageableResponse;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@FeignClient(name = "pessoas-api", url = "${api.pessoas.url}") // URL configurada no application.properties
public interface PessoaApiClient {

    // ==========================================================
    // === NOVOS MÉTODOS PARA PESSOA FÍSICA              ========
    // ==========================================================
    // Assumindo que a pessoas-api tem um endpoint /pessoaFisica
    // E que os DTOs de request/response são os mesmos (PessoaApiRequest/PessoaApiResponse)

    @PostMapping("/pessoaFisica")
    ResponseEntity<PessoaApiResponse> insertPessoaFisica(@RequestBody @Valid PessoaApiRequest dados);

    @PutMapping("/pessoaFisica/{id}")
    ResponseEntity<PessoaApiResponse> updatePessoaFisica(@PathVariable("id") Long id, @RequestBody @Valid PessoaApiRequest dados);

    @GetMapping("/pessoaFisica/{id}")
    ResponseEntity<PessoaApiResponse> findPessoaFisicaById(@PathVariable("id") Long id);

    @DeleteMapping("/pessoaFisica/{id}")
    ResponseEntity<Void> deletePessoaFisica(@PathVariable("id") Long id);

    // ==========================================================


    // ==========================================================
    // === MÉTODOS PARA PESSOA JURÍDICA                       ===
    // ==========================================================
    @PostMapping("/pessoaJuridica")
    ResponseEntity<PessoaJuridicaApiResponse> insertPessoaJuridica(@RequestBody @Valid PessoaJuridicaApiRequest dados);

    @PutMapping("/pessoaJuridica/{id}")
    ResponseEntity<PessoaJuridicaApiResponse> updatePessoaJuridica(
            @PathVariable("id") Long id,
            @RequestBody @Valid PessoaJuridicaApiRequest dados);

    @DeleteMapping("/pessoaJuridica/{id}")
    ResponseEntity<Void> deletePessoaJuridica(@PathVariable("id") Long id);

    // ==========================================================

    // ESTA BUSCA É MISTA BUSCA EM EQUIPES E DE EQUIPES BUSCA AS PESSOAS NA PESSOAS-API
    @GetMapping("/pessoa/{id}") // Corresponde ao endpoint na pessoas-api
    DadosPessoasfjReduzRcd findPessoaById(@PathVariable("id") Long id);

    @GetMapping("/pessoa/{id}/completo") // << Novo caminho para diferenciar
    DadosPessoasGeralRcd findPessoaCompletaById(@PathVariable("id") Long id);

    /*
        ESTA TAMBÉM É MISTA BUSCA EM EQUIPES E DE EQUIPES BUSCA AS PESSOAS NA PESSOAS-API, SÓ QUE TRAZ UMA LISTA DE PESSOAS

         Endpoint para buscar VÁRIAS pessoas por uma lista de IDs.
         Isso é MUITO mais eficiente do que fazer uma chamada por vez.
         A API de pessoas deve ter um endpoint como /pessoas?ids=1,2,3
     */

    // Este método pega pessoas fisica e juridica
    @GetMapping("/pessoa")
    List<DadosPessoasfjReduzRcd> findPessoasByIds(@RequestParam("ids") Set<Long> ids);

    // Este método pega pessoas fisica
    @GetMapping("/pessoa")
    List<DadosPessoasReduzidoRcd> findPessoasFisicaByIds(@RequestParam("ids") Set<Long> ids);

    /* ESTA BUSCA É DIRETO NA PESSOAS-API (PRECISA TER UM END POINT NO CONTROLLER
    * PARA ESTE CASO ELE CHAMA pesquisarPorNomeCpfCnpj NO PessoasController
    * */
    @GetMapping("/pessoa/pesquisar") // <<< Caminho para o endpoint unificado
    ResponseEntity<List<?>> pesquisarPorTermo(
            @RequestParam("termo") String termo,
            @RequestParam(value = "completo", required = false) Boolean completo
    );

    @GetMapping("/pessoa/pesquisar")
    ResponseEntity<List<DadosPessoasfjReduzRcd>> pesquisarPessoasReduzidoPorTermo(
            @RequestParam("termo") String termo,
            @RequestParam(value = "completo", required = false) Boolean completo
    );

    @GetMapping("/pessoa/filtrar")
    PageableResponse<DadosPessoasGeralRcd> filtrarPessoas(
            // Parâmetros do PessoaFilter desmembrados
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "nome", required = false) String nome,
            @RequestParam(value = "cpf", required = false) String cpf,
            @RequestParam(value = "cnpj", required = false) String cnpj,
            @RequestParam(value = "dataNascimento", required = false) String dataNascimento,

            // Parâmetros de paginação
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam(value = "sort", required = false) String[] sort
    );

    @GetMapping("/pessoa/filtrar")
    PageableResponse<DadosPessoasReduzidoRcd> filtrarPessoasFisica(
            // Parâmetros do PessoaFilter desmembrados
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "nome", required = false) String nome,
            @RequestParam(value = "cpf", required = false) String cpf,
            @RequestParam(value = "dataNascimento", required = false) String dataNascimento,

            // Parâmetros de paginação
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam(value = "sort", required = false) String[] sort
    );

    @GetMapping("/pessoaFisica/filtrar")
    Page<DadosPessoasReduzidoRcd> filtrarPessoasFisica(
        @RequestParam(value = "id", required = false) Long id,
        @RequestParam(value = "nome", required = false) String nome,
        @RequestParam(value = "cpf", required = false) String cpf,
        @RequestParam(value = "dataNascimento", required = false) String dataNascimento,
        Pageable pageable
    );


    // ==========================================================
    // === MÉTODO GENÉRICO PARA DELETAR PESSOA (PF OU PJ)     ===
    // ==========================================================
    /**
     * Deleta uma pessoa (física ou jurídica) pelo seu ID.
     * Este método chama o endpoint genérico da pessoas-api que já lida
     * com a exclusão em cascata de endereços, contatos, etc.
     */
    @DeleteMapping("/pessoa/{id}")
    ResponseEntity<Void> deletePessoa(@PathVariable("id") Long id);

}
