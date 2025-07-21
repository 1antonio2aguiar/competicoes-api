package codiub.competicoes.api.controller.pessoas;

import codiub.competicoes.api.DTO.campeonato.DadosListCampeonatoRcd;
import codiub.competicoes.api.DTO.pessoas.DadosPessoasRcd;
import codiub.competicoes.api.DTO.pessoas.DadosPessoasReduzidoRcd;
import codiub.competicoes.api.DTO.pessoas.pessoasfj.DadosPessoasGeralRcd;
import codiub.competicoes.api.DTO.pessoas.pessoasfj.DadosPessoasfjReduzRcd;
import codiub.competicoes.api.client.PessoaApiClient;
import codiub.competicoes.api.entity.pessoas.Pessoas;
import codiub.competicoes.api.filter.pessoas.PessoaFilter;
import codiub.competicoes.api.filter.pessoas.PessoasFilter;
import codiub.competicoes.api.repository.pessoas.PessoasRepository;
import codiub.competicoes.api.service.pessoas.PessoasService;
import codiub.competicoes.api.utils.PageableResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.List;

@RestController
@RequestMapping("/pessoas")
public class PessoasController {
    @Autowired
    private PessoasRepository pessoasRepository;
    @Autowired
    private PessoasService pessoasService;
    @Autowired
    PessoaApiClient pessoaApiClient;

    @GetMapping("/pesquisarPorNomeCpfCnpj")  // ESTE JÁ É DOS NOVOS PEGANDO DA PESSOAS-API
    public ResponseEntity<List<DadosPessoasfjReduzRcd>> pesquisarPessoas(
            @RequestParam("termo") String termo) {

        // 1. O controller recebe a chamada externa.
        // 2. Ele usa o cliente Feign para chamar a outra API (pessoas-api).
        ResponseEntity<List<DadosPessoasfjReduzRcd>> responseFromPessoasApi = pessoaApiClient.pesquisarPorNomeCpfCnpj(termo);

        // 3. Ele retorna a resposta obtida para quem o chamou.
        return responseFromPessoasApi;
    }

    @GetMapping("/filtrarPessoas")
    public ResponseEntity<PageableResponse<DadosPessoasGeralRcd>> filtrar(
            PessoaFilter filter, // O Spring ainda popula este objeto, o que é ótimo!
            Pageable pageable
    ) {

        System.err.println("Filter " + filter);

        // Converter o objeto Sort para o formato de String[]
        String[] sortParams = pageable.getSort().stream()
                .map(order -> order.getProperty() + "," + order.getDirection().name())
                .toArray(String[]::new);
        String[] sortToSend = sortParams.length > 0 ? sortParams : null;



        // Converter o campo Date para uma String no formato YYYY-MM-DD
        String dataNascimentoStr = null;
        if (filter.getDataNascimento() != null) {
            // Formato padrão que o Spring MVC na API de destino deve entender
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            dataNascimentoStr = sdf.format(filter.getDataNascimento());
        }

        // Chamar o método CORRIGIDO do Feign Client, passando os valores individuais
        PageableResponse<DadosPessoasGeralRcd> resultados = pessoaApiClient.filtrarPessoas(
                filter.getId(),
                filter.getNome(),
                filter.getCpf(),
                filter.getCnpj(),
                dataNascimentoStr, // Passa a data formatada como string
                pageable.getPageNumber(),
                pageable.getPageSize(),
                sortToSend
        );

        return ResponseEntity.ok(resultados);
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////


    // DAQUI PARA BAIXO PEGANDO LOCALMENTE SEM BUSCAR NO PESSOAS-API

    @GetMapping
    public Page<DadosPessoasRcd> findall(@PageableDefault (sort={"nome"}) Pageable paginacao){
        return pessoasRepository.findAll(paginacao).map(DadosPessoasRcd::fromPessoas);
    }
    @GetMapping("/filter")
    public Page<DadosPessoasReduzidoRcd> pesquisar(PessoasFilter filter, Pageable pageable) {
        return pessoasService.pesquisar(filter, pageable);
    }

    /*
        Este metodo filtra/traz apenas pessoas que não pertencem a nenhuma equipe.
        ele esta sendo usado na tela de cadastro de atletas, onde um mesmo atleta não pode pertencer a mais de uma equipe.
     */
    @GetMapping("/pessoaNotInEquipes")
    public Page<DadosPessoasReduzidoRcd> pesquisarByPessoa(PessoasFilter filter, Pageable pageable) {
        return pessoasService.pessoaNotInEquipes(filter, pageable);
    }
    @GetMapping("/{id}")
    public ResponseEntity<DadosPessoasRcd> findById(@PathVariable Long id) {
        DadosPessoasRcd dadosPessoasRcd = pessoasService.findById(id);
        return dadosPessoasRcd != null
                ? ResponseEntity.ok(dadosPessoasRcd)
                : ResponseEntity.notFound().build();
    }
}


