package codiub.competicoes.api.client;

import codiub.competicoes.api.DTO.pessoas.DadosPessoasReduzidoRcd;
import codiub.competicoes.api.DTO.pessoas.pessoasfj.DadosPessoasGeralRcd;
import codiub.competicoes.api.DTO.pessoas.pessoasfj.DadosPessoasfjReduzRcd;
import codiub.competicoes.api.utils.PageableResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

@FeignClient(name = "pessoas-api", url = "${api.pessoas.url}") // URL configurada no application.properties
public interface PessoaApiClient {

    // ESTA BUSCA É MISTA BUSCA EM EQUIPES E DE EQUIPES BUSCA AS PESSOAS NA PESSOAS-API
    @GetMapping("/pessoa/{id}") // Corresponde ao endpoint na pessoas-api
    DadosPessoasfjReduzRcd findPessoaById(@PathVariable("id") Long id);

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
    @GetMapping("/pessoa")
    List<DadosPessoasfjReduzRcd> pesquisarPorNomeCpfCnpj(@RequestParam("termo") String termo);

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

    ///

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


    // TRABALHANDO AQUI. >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    @GetMapping("/pessoaFisica/filtrar")
    Page<DadosPessoasReduzidoRcd> filtrarPessoasFisica(
        @RequestParam(value = "id", required = false) Long id,
        @RequestParam(value = "nome", required = false) String nome,
        @RequestParam(value = "cpf", required = false) String cpf,
        @RequestParam(value = "dataNascimento", required = false) String dataNascimento,
        Pageable pageable
    );
}
