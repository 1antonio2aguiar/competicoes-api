package codiub.competicoes.api.controller.pessoas;

import codiub.competicoes.api.DTO.pessoas.pessoa.DadosPessoaJuridicaReqRespRcd;
import codiub.competicoes.api.DTO.pessoas.pessoa.PessoaJuridicaApiRequest;
import codiub.competicoes.api.DTO.pessoas.pessoa.PessoaJuridicaApiResponse;
import codiub.competicoes.api.DTO.pessoas.pessoasfj.DadosPessoasGeralRcd;
import codiub.competicoes.api.DTO.pessoas.pessoasfj.DadosPessoasfjReduzRcd;
import codiub.competicoes.api.client.PessoaApiClient;
import codiub.competicoes.api.entity.pessoas.TiposPessoas;
import codiub.competicoes.api.filter.pessoas.PessoaFilter;
import codiub.competicoes.api.repository.InscricaoRepository;
import codiub.competicoes.api.repository.pessoas.PessoasRepository;
import codiub.competicoes.api.service.pessoas.PessoasService;
import codiub.competicoes.api.utils.PageableResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
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
    @Autowired private InscricaoRepository inscricaoRepository;
    @Autowired
    PessoaApiClient pessoaApiClient;

    @GetMapping("/{id}")
    public ResponseEntity<DadosPessoasfjReduzRcd> buscarPessoaPorId(@PathVariable Long id) {
        DadosPessoasfjReduzRcd pessoaEncontrada = pessoaApiClient.findPessoaById(id);
        return ResponseEntity.ok(pessoaEncontrada);
    }

    // **** NOVO ENDPOINT PARA BUSCA COMPLETA POR ID ****
    /**
     * Endpoint para buscar os dados completos de uma única pessoa pelo seu ID.
     * URL de acesso: GET http://localhost:8080/pessoas/{id}/completo
     */
    @GetMapping("/{id}/completo")
    public ResponseEntity<DadosPessoasGeralRcd> buscarPessoaCompletaPorId(@PathVariable Long id) {
        DadosPessoasGeralRcd pessoaEncontrada = pessoaApiClient.findPessoaCompletaById(id);
        return ResponseEntity.ok(pessoaEncontrada);
    }

    @GetMapping("/pesquisar") // << Um endpoint único e claro
    public ResponseEntity<?> pesquisarPessoasPorTermo(
            @RequestParam("termo") String termo,
            @RequestParam(value = "completo", required = false, defaultValue = "false") boolean completo
    ) {
        System.err.println("Esta entando aqui " + termo);
        // Chama o método unificado do Feign Client, passando o parâmetro 'completo'
        ResponseEntity<List<?>> response = pessoaApiClient.pesquisarPorTermo(termo, completo);

        // Simplesmente repassa a resposta recebida
        return response;
    }

    // ESTE JÁ É DOS NOVOS PEGANDO DA PESSOAS-API
    @GetMapping("/filtrarPessoas")
    public ResponseEntity<PageableResponse<DadosPessoasGeralRcd>> filtrar(
            PessoaFilter filter, // O Spring ainda popula este objeto, o que é ótimo!
            Pageable pageable
    ) {

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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        pessoasService.delete(id);
        // Retorna 204 No Content, que é o padrão para um DELETE bem-sucedido.
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/tipos-pessoas")
    public ResponseEntity<List<TiposPessoas>> listarTiposPessoas() {
        List<TiposPessoas> tipos = pessoaApiClient.listarTiposPessoas();
        System.err.println("passou aqui >>>>>>>>>>>>>>>>>>>>>>");
        return ResponseEntity.ok(tipos);
    }
}


