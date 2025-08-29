package codiub.competicoes.api.service;

import codiub.competicoes.api.DTO.inscricoes.DadosInsertInscricoesRcd;
import codiub.competicoes.api.DTO.inscricoes.DadosListInscricoesRcd;
import codiub.competicoes.api.DTO.inscricoes.DadosUpdateInscricoesRcd;
import codiub.competicoes.api.DTO.pessoas.pessoasfj.DadosPessoasfjReduzRcd;
import codiub.competicoes.api.client.PessoaApiClient;
import codiub.competicoes.api.entity.*;
import codiub.competicoes.api.entity.enuns.StatusInscricao;
import codiub.competicoes.api.entity.enuns.StatusTipoInscricao;
import codiub.competicoes.api.filter.InscricaoFilter;
import codiub.competicoes.api.repository.*;
import codiub.competicoes.api.service.exceptions.DatabaseException;
import codiub.competicoes.api.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class InscricaoService {
    @Autowired private InscricaoRepository inscricaoRepository;
    @Autowired private EmpresaRepository empresaRepository;
    @Autowired private ProvaRepository provaRepository;
    @Autowired private AtletaRepository atletaRepository;
    @Autowired private PontuacaoRepository pontuacaoRepository;
    @Autowired private PessoaApiClient pessoaApiClient;
    public InscricaoService(InscricaoRepository inscricaoRepository) {
        this.inscricaoRepository = inscricaoRepository;
    }

    //Metodo filtrar
    @Transactional(readOnly = true)
    public Page<DadosListInscricoesRcd> pesquisar(InscricaoFilter filter, Pageable pageable) {
        // 1. Busca localmente as inscrições da página.
        Page<Inscricoes> inscricaoPage = inscricaoRepository.filtrar(filter, pageable);
        List<Inscricoes> inscricoesDaPagina = inscricaoPage.getContent();

        // Se a página estiver vazia, retorna imediatamente.
        if (inscricoesDaPagina.isEmpty()) {
            return Page.empty(pageable);
        }

        // 2. Coleta os 'pessoaId' de cada atleta inscrito para buscar os nomes.
        Set<Long> pessoaIds = inscricoesDaPagina.stream()
                .map(inscricao -> inscricao.getAtleta().getPessoaId()) // Navega: Inscricao -> Atleta -> pessoaId
                .collect(Collectors.toSet());

        // 3. ORQUESTRAÇÃO: Faz uma única chamada para a 'pessoas-api'.
        // Supondo que o DTO retornado seja o DadosPessoasfjReduzidoRcd
        List<DadosPessoasfjReduzRcd> pessoasInfo = pessoaApiClient.findPessoasByIds(pessoaIds);

        // 4. Cria um Mapa (ID da Pessoa -> Nome da Pessoa) para busca rápida.
        Map<Long, String> mapaIdParaNome = pessoasInfo.stream()
                .collect(Collectors.toMap(DadosPessoasfjReduzRcd::id, DadosPessoasfjReduzRcd::nome));

        // 5. COMBINAÇÃO: Constrói a lista final de DTOs.
        List<DadosListInscricoesRcd> inscricaoDTOList = inscricoesDaPagina.stream()
                .map(inscricao -> {
                    // Para cada inscrição, pega o ID da pessoa associada ao atleta
                    Long pessoaIdDoAtleta = inscricao.getAtleta().getPessoaId();
                    // Busca o nome correspondente no mapa
                    String nomeDoAtleta = mapaIdParaNome.getOrDefault(pessoaIdDoAtleta, "Nome não encontrado");
                    // Chama o método de fábrica passando a inscrição e o nome encontrado
                    return DadosListInscricoesRcd.fromInscricao(inscricao, nomeDoAtleta);
                })
                .collect(Collectors.toList());

        // 6. Retorna a página final com os dados enriquecidos.
        return new PageImpl<>(inscricaoDTOList, pageable, inscricaoPage.getTotalElements());
    }

    // Inscricao por id
    @Transactional(readOnly = true)
    public DadosListInscricoesRcd findById(Long id) {
        // 1. Busca a inscrição. O findById padrão do JpaRepository é suficiente.
        Optional<Inscricoes> inscricaoOptional = inscricaoRepository.findById(id);

        // Se não encontrar, retorna nulo.
        if (inscricaoOptional.isEmpty()) {
            return null;
        }

        Inscricoes inscricao = inscricaoOptional.get();

        // 2. A MÁGICA: "Acorde" os proxies ENQUANTO a transação está ABERTA.
        // Ao chamar um getter em um objeto LAZY, o Hibernate o carrega do banco.
        String equipeNome = inscricao.getAtleta().getEquipe().getNome();
        Long pessoaId = inscricao.getAtleta().getPessoaId();

        // 3. ORQUESTRAÇÃO: Agora que os dados locais estão em memória, chame a outra API.
        List<DadosPessoasfjReduzRcd> pessoaInfoList = pessoaApiClient.findPessoasByIds(Set.of(pessoaId));

        String atletaNome = "Nome não encontrado";
        if (pessoaInfoList != null && !pessoaInfoList.isEmpty()) {
            atletaNome = pessoaInfoList.get(0).nome();
        }

        // 4. Construa e retorne o DTO.
        // O método de fábrica agora pode usar 'equipeNome' diretamente, pois já o carregamos.
        return DadosListInscricoesRcd.fromInscricao(inscricao, atletaNome);
    }

    @Transactional(readOnly = true)
    public Page<DadosListInscricoesRcd> findall(Pageable paginacao) {
        // 1. Busca a página de inscrições.
        Page<Inscricoes> inscricaoPage = inscricaoRepository.findAll(paginacao);
        List<Inscricoes> inscricoesDaPagina = inscricaoPage.getContent();

        // Se a página estiver vazia, retorna imediatamente.
        if (inscricoesDaPagina.isEmpty()) {
            return Page.empty(paginacao);
        }

        // 2. Coleta os 'pessoaId' de cada atleta inscrito.
        Set<Long> pessoaIds = inscricoesDaPagina.stream()
                .map(inscricao -> inscricao.getAtleta().getPessoaId())
                .collect(Collectors.toSet());

        // 3. ORQUESTRAÇÃO: Chama a 'pessoas-api' para buscar os nomes.
        List<DadosPessoasfjReduzRcd> pessoasInfo = pessoaApiClient.findPessoasByIds(pessoaIds);

        // 4. Cria um Mapa (ID -> Nome) para busca rápida.
        Map<Long, String> mapaIdParaNome = pessoasInfo.stream()
                .collect(Collectors.toMap(DadosPessoasfjReduzRcd::id, DadosPessoasfjReduzRcd::nome));

        // 5. COMBINAÇÃO: Constrói a lista final de DTOs.
        List<DadosListInscricoesRcd> inscricaoDTOList = inscricoesDaPagina.stream()
                .map(inscricao -> {
                    Long pessoaIdDoAtleta = inscricao.getAtleta().getPessoaId();
                    String nomeDoAtleta = mapaIdParaNome.getOrDefault(pessoaIdDoAtleta, "Nome não encontrado");
                    return DadosListInscricoesRcd.fromInscricao(inscricao, nomeDoAtleta);
                })
                .collect(Collectors.toList());

        // 6. Retorna a página final com os dados enriquecidos.
        return new PageImpl<>(inscricaoDTOList, paginacao, inscricaoPage.getTotalElements());
    }

    //Insert
    public Inscricoes insert(DadosInsertInscricoesRcd dados) {
        Inscricoes inscricao = new Inscricoes();

        BeanUtils.copyProperties(dados, inscricao, "id");

        //Busco a empresa
        if (dados.empresaId() != null && dados.empresaId() != 0) {
            Empresa empresa = empresaRepository.findById(dados.empresaId()).get();
            inscricao.setEmpresa(empresa);
        }
        //Busco a prova
        Prova prova = provaRepository.findById(dados.provaId()).get();
        inscricao.setProva(prova);

        //Busco o atleta
        Atleta atleta = atletaRepository.findById(dados.atletaId()).get();
        inscricao.setAtleta(atleta);

        inscricao.setStatus(StatusInscricao.toStatusInscricaoEnum(dados.status()));
        inscricao.setTipoInscricao(StatusTipoInscricao.toStatusTipoInscricaoEnum(dados.statusTipoInscricao()));

        //System.err.println("dados " + inscricao);

        return inscricaoRepository.save(inscricao);
    }

    //Update
    public Inscricoes update(Long id, DadosUpdateInscricoesRcd dados) {
        Inscricoes inscricaoUpd = inscricaoRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Inscrição não cadastrada. Id: " + id));

        BeanUtils.copyProperties(dados, inscricaoUpd, "id");
        inscricaoUpd.setStatus(StatusInscricao.toStatusInscricaoEnum(dados.status()));
        inscricaoUpd.setTipoInscricao(StatusTipoInscricao.toStatusTipoInscricaoEnum(dados.statusTipoInscricao()));

        //Pontuação
        //Pontuacao pontuacao = pontuacaoRepository.findById(dados.pontuacaoId()).get();
        //inscricaoUpd.setPontuacao(pontuacao);

        //dados.getResultadoAsLong().ifPresent(inscricaoUpd::setResultado);

        return inscricaoRepository.save(inscricaoUpd);
    }

    // Delete
    public void delete(Long id) {
        Inscricoes inscricaoDel = inscricaoRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Inscrição não cadastrada. Id: " + id));
        try {
            inscricaoRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ObjectNotFoundException(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }
}
