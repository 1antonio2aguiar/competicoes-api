package codiub.competicoes.api.service.pessoas;

import codiub.competicoes.api.DTO.pessoas.pessoa.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import codiub.competicoes.api.service.client.PessoaRemoteClientService; // O novo cliente

import codiub.competicoes.api.entity.pessoas.pessoa.PessoaFisica;
import codiub.competicoes.api.filter.pessoas.PessoaFisicaFilter;
import codiub.competicoes.api.repository.pessoas.PessoaFisicaRepository;
import codiub.competicoes.api.repository.pessoas.pessoaFisica.custon.PessoaFisicaCustonRepository;
import codiub.competicoes.api.service.exceptions.DatabaseException;
import codiub.competicoes.api.service.exceptions.ObjectNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PessoaFisicaService {
    @Autowired private PessoaFisicaRepository pessoaFisicaRepository;
    @Autowired private PessoaFisicaCustonRepository pessoaFisicaCustonRepository;
    @Autowired
    private PessoaRemoteClientService pessoaRemoteClient;

    public PessoaFisicaService(PessoaFisicaRepository pessoaFisicaRepository) {
        this.pessoaFisicaRepository = pessoaFisicaRepository;
    }

    //Metodo filtrar
    public Page<DadosPessoaFisicaReduzRcd> pesquisar(PessoaFisicaFilter filter, Pageable pageable) {
        Page<PessoaFisica> pessoaPage = pessoaFisicaRepository.filtrar(filter, pageable);

        // Mapeia a lista de Pessoas para uma lista de DadosPessoasRcd usando o método de fábrica
        List<DadosPessoaFisicaReduzRcd> pessoasDTOList = pessoaPage.getContent().stream()
                .map(DadosPessoaFisicaReduzRcd::fromPessoaFisica)
                .collect(Collectors.toList());

        // Cria um novo Page<DadosPessoasRcd> com os dados mapeados
        return new PageImpl<>(pessoasDTOList, pageable, pessoaPage.getTotalElements());
    }

    public Page<DadosPessoaFisicaReduzRcd> pessoaFisicaNotInEquipes(PessoaFisicaFilter filter, Pageable pageable) {
        // Recupera somente pessoas que não pertecem a nenhuma equipe.
        Page<PessoaFisica> pessoaPage = pessoaFisicaCustonRepository.pessoaFisicaNotInEquipes(filter, pageable);

        // Mapeia a lista de Pessoas para uma lista de DadosPessoasRcd usando o método de fábrica
        List<DadosPessoaFisicaReduzRcd> pessoasDTOList = pessoaPage.getContent().stream()
                .map(DadosPessoaFisicaReduzRcd::fromPessoaFisica)
                .collect(Collectors.toList());

        // Cria um novo Page<DadosPessoasRcd> com os dados mapeados
        return new PageImpl<>(pessoasDTOList, pageable, pessoaPage.getTotalElements());
    }

    // Pessoa fisica por id
    /*public DadosListPessoaFisicaRcd findById(Long id) {
        Optional<PessoaFisica> pfOptional = pessoaFisicaRepository.findById(id);
        return pfOptional.map(DadosListPessoaFisicaRcd::fromPessoaFisica).orElse(null);
    }*/
    @Transactional(readOnly = true)
    public DadosListPessoaFisicaRcd findById(Long id) {
        PessoaApiResponse pessoaDaApi = pessoaRemoteClient.buscarPessoaFisicaPorId(id);
        if (pessoaDaApi == null) {
            return null; // Ou lançar ObjectNotFoundException
        }
        // Mapeia do DTO da API para o DTO de listagem do seu controller
        return new DadosListPessoaFisicaRcd(
                pessoaDaApi.id(),
                pessoaDaApi.nome(),
                pessoaDaApi.fisicaJuridica(),
                pessoaDaApi.situacao(),
                pessoaDaApi.tipoPessoaId(),
                pessoaDaApi.tipoPessoaNome(),
                pessoaDaApi.cpf(),
                pessoaDaApi.sexo(),
                pessoaDaApi.estadoCivil(),
                pessoaDaApi.dataNascimento() ,
                pessoaDaApi.nomeMae(),
                pessoaDaApi.nomePai(),
                pessoaDaApi.observacao()
        );
    }

    //Insert
    // O método insert mapeia para o DTO da API, chama o cliente e mapeia a resposta
    @Transactional // Pode ser útil se você fizer algo localmente após a chamada
    public DadosListPessoaFisicaRcd insert(DadosInsertPessoaFisicaRcd dadosEntradaController) {
        // 1. Mapear o DTO de entrada do seu controller para o DTO de requisição da API de Pessoas
        PessoaApiRequest dadosParaApi = new PessoaApiRequest(
                dadosEntradaController.nome(),
                dadosEntradaController.fisicaJuridica(),
                dadosEntradaController.observacao(),
                dadosEntradaController.situacao(),
                dadosEntradaController.tipoPessoaId(),
                dadosEntradaController.cpf(),
                dadosEntradaController.sexo(),
                dadosEntradaController.estadoCivil(),
                dadosEntradaController.dataNascimento() != null ? dadosEntradaController.dataNascimento().format(DateTimeFormatter.ISO_LOCAL_DATE) : null, // Formata LocalDate para "YYYY-MM-DD"
                dadosEntradaController.nomeMae(),
                dadosEntradaController.nomePai()
        );

        // 2. Chamar o cliente para interagir com a API remota
        PessoaApiResponse pessoaSalvaRemotamente = pessoaRemoteClient.inserirPessoaFisica(dadosParaApi);

        // 3. Mapear o DTO de resposta da API para o DTO de listagem do seu controller
        if (pessoaSalvaRemotamente == null) {
            throw new RuntimeException("API remota de pessoas não retornou dados após inserção.");
        }
        return new DadosListPessoaFisicaRcd(
                pessoaSalvaRemotamente.id(),
                pessoaSalvaRemotamente.nome(),
                pessoaSalvaRemotamente.fisicaJuridica(),
                pessoaSalvaRemotamente.situacao(),
                pessoaSalvaRemotamente.tipoPessoaId(),
                pessoaSalvaRemotamente.tipoPessoaNome(),
                pessoaSalvaRemotamente.cpf(),
                pessoaSalvaRemotamente.sexo(),
                pessoaSalvaRemotamente.estadoCivil(),
                pessoaSalvaRemotamente.dataNascimento() ,
                pessoaSalvaRemotamente.nomeMae(),
                pessoaSalvaRemotamente.nomePai(),
                pessoaSalvaRemotamente.observacao()
        );
    }

    // Delete
    public void delete(Long id){
        PessoaFisica pessoaFisicaDel = pessoaFisicaRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Pessoa não cadastrada. Id: " + id));
        try {
            pessoaFisicaRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ObjectNotFoundException(id);
        } catch (DataIntegrityViolationException e){
            throw new DatabaseException(e.getMessage());
        }
    }

}