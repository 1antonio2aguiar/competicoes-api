package codiub.competicoes.api.service;

import codiub.competicoes.api.DTO.apuracao.DadosInsertApuracoesRcd;
import codiub.competicoes.api.DTO.apuracao.DadosListApuracaoAndInscricaoRcd;
import codiub.competicoes.api.DTO.apuracao.DadosListApuracoesRcd;
import codiub.competicoes.api.DTO.apuracao.DadosUpdateApuracoesRcd;
import codiub.competicoes.api.DTO.inscricoes.DadosInsertInscricoesRcd;
import codiub.competicoes.api.DTO.pessoas.pessoasfj.DadosPessoasfjReduzRcd;
import codiub.competicoes.api.client.PessoaApiClient;
import codiub.competicoes.api.entity.*;
import codiub.competicoes.api.entity.enuns.StatusApuracao;
import codiub.competicoes.api.entity.enuns.StatusInscricao;
import codiub.competicoes.api.entity.enuns.StatusTipoInscricao;
import codiub.competicoes.api.filter.ApuracaoFilter;
import codiub.competicoes.api.repository.*;
import codiub.competicoes.api.repository.apuracao.custon.ApuracaoCustonRepository;
import codiub.competicoes.api.service.exceptions.DatabaseException;
import codiub.competicoes.api.service.exceptions.ObjectNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ApuracaoService {
    @Autowired
    private ApuracaoRepository apuracaoRepository;
    @Autowired
    private EmpresaRepository empresaRepository;
    @Autowired
    private ProvaRepository provaRepository;
    @Autowired
    private InscricaoRepository inscricaoRepository;
    @Autowired
    private InscricaoService inscricaoService;
    @Autowired
    private AtletaRepository atletaRepository;
    @Autowired
    private PontuacaoRepository pontuacaoRepository;
    @Autowired
    private ApuracaoCustonRepository apuracaoCustonRepository;
    @Autowired
    private PessoaApiClient pessoaApiClient;
    @Autowired
    private ObjectMapper objectMapper;

    public ApuracaoService(ApuracaoRepository apuracaoRepository) {
        this.apuracaoRepository = apuracaoRepository;
    }

    //Metodo filtrar
    @Transactional(readOnly = true) // Boa prática adicionar a anotação aqui
    public Page<DadosListApuracoesRcd> pesquisar(ApuracaoFilter filter, Pageable pageable) {
        // 1. Busca localmente a página de Apuracoes.
        Page<Apuracao> apuracaoPage = apuracaoRepository.filtrar(filter, pageable);
        List<Apuracao> apuracoesDaPagina = apuracaoPage.getContent();

        // Se a página estiver vazia, retorna imediatamente.
        if (apuracoesDaPagina.isEmpty()) {
            return Page.empty(pageable);
        }

        // 2. Coleta os 'pessoaId' de cada atleta envolvido na apuração.
        Set<Long> pessoaIds = apuracoesDaPagina.stream()
                .map(apuracao -> apuracao.getAtleta().getPessoaId()) // Navega: Apuracao -> Atleta -> pessoaId
                .collect(Collectors.toSet());

        // 3. ORQUESTRAÇÃO: Chama a 'pessoas-api' uma única vez com todos os IDs.
        // Usando o DTO que você especificou: DadosPessoasfjReduzidoRcd
        List<DadosPessoasfjReduzRcd> pessoasInfo = pessoaApiClient.findPessoasByIds(pessoaIds);

        // 4. Cria um Mapa (ID da Pessoa -> Nome da Pessoa) para busca rápida.
        Map<Long, String> mapaIdParaNome = pessoasInfo.stream()
                .collect(Collectors.toMap(DadosPessoasfjReduzRcd::id, DadosPessoasfjReduzRcd::nome));

        // 5. COMBINAÇÃO: Constrói a lista final de DTOs com os nomes enriquecidos.
        List<DadosListApuracoesRcd> apuracaoDTOList = apuracoesDaPagina.stream()
                .map(apuracao -> {
                    Long pessoaIdDoAtleta = apuracao.getAtleta().getPessoaId();
                    String nomeDoAtleta = mapaIdParaNome.getOrDefault(pessoaIdDoAtleta, "Nome não encontrado");

                    // Supondo que o método de fábrica foi ajustado para aceitar o nome
                    return DadosListApuracoesRcd.fromApuracao(apuracao, nomeDoAtleta);
                })
                .collect(Collectors.toList());

        // 6. Retorna a página final com os dados completos.
        return new PageImpl<>(apuracaoDTOList, pageable, apuracaoPage.getTotalElements());
    }

    // Apuracoes por id
    @Transactional(readOnly = true)
    public DadosListApuracoesRcd findById(Long id) {
        // 1. Busca a apuração no banco de dados local.
        Optional<Apuracao> apuracaoOptional = apuracaoRepository.findById(id);

        // Se a apuração não for encontrada, retorna nulo.
        if (apuracaoOptional.isEmpty()) {
            return null;
        }

        Apuracao apuracao = apuracaoOptional.get();

        // 2. "Acorda" os proxies necessários ENQUANTO a transação está aberta.
        // Acessar qualquer campo do atleta é suficiente para carregá-lo.
        // Isso evita a LazyInitializationException.
        Long pessoaId = apuracao.getAtleta().getPessoaId();

        // 3. ORQUESTRAÇÃO: Busca os dados da pessoa na outra API.
        // Usando o DTO 'DadosPessoasfjReduzRcd' como instruído.
        List<DadosPessoasfjReduzRcd> pessoaInfoList = pessoaApiClient.findPessoasByIds(Set.of(pessoaId));

        // 4. Extrai o nome da pessoa da resposta.
        String atletaNome = "Nome não encontrado"; // Valor padrão
        if (pessoaInfoList != null && !pessoaInfoList.isEmpty()) {
            atletaNome = pessoaInfoList.get(0).nome();
        }

        // 5. Usa o método de fábrica para criar o DTO com o nome enriquecido.
        return DadosListApuracoesRcd.fromApuracao(apuracao, atletaNome);
    }

    @Transactional(readOnly = true)
    public Page<DadosListApuracoesRcd> findall(Pageable paginacao) {
        // 1. Busca a página de Apuracoes usando findAll.
        Page<Apuracao> apuracaoPage = apuracaoRepository.findAll(paginacao);
        List<Apuracao> apuracoesDaPagina = apuracaoPage.getContent();

        // Se a página estiver vazia, retorna imediatamente.
        if (apuracoesDaPagina.isEmpty()) {
            return Page.empty(paginacao);
        }

        // 2. Coleta os 'pessoaId' de cada atleta.
        Set<Long> pessoaIds = apuracoesDaPagina.stream()
                .map(apuracao -> apuracao.getAtleta().getPessoaId())
                .collect(Collectors.toSet());

        // 3. ORQUESTRAÇÃO: Chama a 'pessoas-api'.
        List<DadosPessoasfjReduzRcd> pessoasInfo = pessoaApiClient.findPessoasByIds(pessoaIds);

        // 4. Cria um Mapa (ID da Pessoa -> Nome da Pessoa) para busca rápida.
        Map<Long, String> mapaIdParaNome = pessoasInfo.stream()
                .collect(Collectors.toMap(DadosPessoasfjReduzRcd::id, DadosPessoasfjReduzRcd::nome));

        // 5. COMBINAÇÃO: Constrói a lista final de DTOs.
        List<DadosListApuracoesRcd> apuracaoDTOList = apuracoesDaPagina.stream()
                .map(apuracao -> {
                    Long pessoaIdDoAtleta = apuracao.getAtleta().getPessoaId();
                    String nomeDoAtleta = mapaIdParaNome.getOrDefault(pessoaIdDoAtleta, "Nome não encontrado");

                    // Chama o método de fábrica ajustado
                    return DadosListApuracoesRcd.fromApuracao(apuracao, nomeDoAtleta);
                })
                .collect(Collectors.toList());

        // 6. Retorna a página final com os dados completos.
        return new PageImpl<>(apuracaoDTOList, paginacao, apuracaoPage.getTotalElements());
    }

    @Transactional(readOnly = true)
    public Page<DadosListApuracaoAndInscricaoRcd> apuracaoAndInscricao(ApuracaoFilter filter, Pageable pageable) {
        Set<Long> idsDePessoasFiltradasPorNome = null;
        String nomeAtletaFilter = filter.getAtletaNome();

        // --- ETAPA 1: PRÉ-FILTRAGEM POR NOME (SE NECESSÁRIO) ---
        if (nomeAtletaFilter != null && !nomeAtletaFilter.isBlank()) {
            // Usa o método existente que retorna ResponseEntity<List<?>>
            ResponseEntity<List<?>> response = pessoaApiClient.pesquisarPorTermo(nomeAtletaFilter, false);

            // Se a busca por nome não retornar nada, a consulta final também será vazia.
            if (response.getBody() == null || response.getBody().isEmpty()) {
                return Page.empty(pageable);
            }

            // Converte a lista de Maps genéricos para a lista de DTOs corretos
            List<DadosPessoasfjReduzRcd> pessoasConvertidas = response.getBody().stream()
                    .map(mapa -> objectMapper.convertValue(mapa, DadosPessoasfjReduzRcd.class))
                    .collect(Collectors.toList());

            // Extrai os IDs da lista já convertida
            idsDePessoasFiltradasPorNome = pessoasConvertidas.stream()
                    .map(DadosPessoasfjReduzRcd::id)
                    .collect(Collectors.toSet());

            // Otimização final: se a conversão resultar em uma lista de IDs vazia.
            if (idsDePessoasFiltradasPorNome.isEmpty()) {
                return Page.empty(pageable);
            }
        }

        // --- ETAPA 2: CONSULTA LOCAL ---
        // A busca no repositório agora recebe a lista de IDs pré-filtrados (pode ser nula)
        Page<DadosListApuracaoAndInscricaoRcd> paginaSemNomes = apuracaoCustonRepository.apuracaoAndInscricao(filter, pageable, idsDePessoasFiltradasPorNome);
        List<DadosListApuracaoAndInscricaoRcd> listaSemNomes = paginaSemNomes.getContent();

        if (listaSemNomes.isEmpty()) {
            return Page.empty(pageable);
        }

        // --- ETAPA 3: ENRIQUECIMENTO DOS DADOS ---
        // Coleta os IDs dos atletas da página atual para buscar os nomes completos
        Set<Long> atletaIdsDaPagina = listaSemNomes.stream()
                .map(DadosListApuracaoAndInscricaoRcd::atletaId)
                .collect(Collectors.toSet());

        // Precisamos converter atletaId para pessoaId
        List<Atleta> atletasDaPagina = atletaRepository.findAllById(atletaIdsDaPagina);
        Set<Long> pessoaIdsParaEnriquecer = atletasDaPagina.stream().map(Atleta::getPessoaId).collect(Collectors.toSet());

        // Faz a chamada final para a 'pessoas-api' para obter os nomes
        List<DadosPessoasfjReduzRcd> pessoasInfo = pessoaApiClient.findPessoasByIds(pessoaIdsParaEnriquecer);
        Map<Long, String> mapaPessoaIdParaNome = pessoasInfo.stream()
                .collect(Collectors.toMap(DadosPessoasfjReduzRcd::id, DadosPessoasfjReduzRcd::nome));

        Map<Long, Long> mapaAtletaIdParaPessoaId = atletasDaPagina.stream()
                .collect(Collectors.toMap(Atleta::getId, Atleta::getPessoaId));

        // Monta a lista final de DTOs, agora com os nomes preenchidos
        List<DadosListApuracaoAndInscricaoRcd> listaComNomes = listaSemNomes.stream()
                .map(dto -> {
                    Long pessoaId = mapaAtletaIdParaPessoaId.get(dto.atletaId());
                    String nome = mapaPessoaIdParaNome.getOrDefault(pessoaId, "Nome não encontrado");

                    // Usa o construtor canônico do record para criar uma nova instância com o nome
                    return new DadosListApuracaoAndInscricaoRcd(
                            dto.inscricaoId(), dto.apuracaoId(), dto.provaId(), dto.equipeNome(),
                            dto.atletaId(), nome, dto.serie(), dto.baliza(),
                            dto.resultado(), dto.tipoInscricao()
                    );
                })
                .collect(Collectors.toList());

        // Retorna a página final com a lista enriquecida e a contagem total correta
        return new PageImpl<>(listaComNomes, pageable, paginaSemNomes.getTotalElements());
    }

    //Insert
    public Apuracao insert(DadosInsertApuracoesRcd dados) {

        System.err.println("CHEGOU NA BAGAÇA: " + dados.inscricaoId() + ". ATUALIZANDO...");

        // --- LÓGICA DE VERIFICAÇÃO ADICIONADA ---
        // 1. Verifica se já existe uma apuração para a inscrição fornecida.
        Optional<Apuracao> apuracaoExistenteOpt = apuracaoRepository.findByInscricaoId(dados.inscricaoId());

        Apuracao apuracao; // Declara a variável que vamos salvar

        if (apuracaoExistenteOpt.isPresent()) {
            // 2. SE JÁ EXISTE: Vamos atualizar (UPDATE)
            System.err.println("APURAÇÃO JÁ EXISTE PARA A INSCRIÇÃO ID: " + dados.inscricaoId() + ". ATUALIZANDO...");
            apuracao = apuracaoExistenteOpt.get();

            // Atualiza apenas os campos que podem mudar em uma re-apuração
            // (resultado, status, pontuação, etc.)
            // Usamos os dados do DTO de INSERT, mas aplicamos em um objeto existente.

            //Busca pontuacao
            Pontuacao pontuacao = pontuacaoRepository.findById(dados.pontuacaoId()).get();
            apuracao.setPontuacao(pontuacao);

            apuracao.setStatus(StatusApuracao.toStatusApuracaoEnum(dados.status()));
            dados.getResultadoAsLong().ifPresent(apuracao::setResultado);

        } else {
            // 3. SE NÃO EXISTE: Vamos criar uma nova (INSERT)
            System.err.println("APURAÇÃO NÃO EXISTE PARA A INSCRIÇÃO ID: " + dados.inscricaoId() + ". CRIANDO NOVA...");
            apuracao = new Apuracao();

            // A lógica original de criação continua aqui
            BeanUtils.copyProperties(dados, apuracao, "id");

            //Busco a empresa
            if (dados.empresaId() != null && dados.empresaId() != 0) {
                Empresa empresa = empresaRepository.findById(dados.empresaId()).get();
                apuracao.setEmpresa(empresa);
            }
            //Busco a prova
            Prova prova = provaRepository.findById(dados.provaId()).get();
            apuracao.setProva(prova);

            //Busco a inscricao
            Inscricoes inscricao = inscricaoRepository.findById(dados.inscricaoId()).get();
            apuracao.setInscricao(inscricao);

            //Busco o atleta
            Atleta atleta = atletaRepository.findById(dados.atletaId()).get();
            apuracao.setAtleta(atleta);

            //Busco pontuacao
            Pontuacao pontuacao = pontuacaoRepository.findById(dados.pontuacaoId()).get();
            apuracao.setPontuacao(pontuacao);

            apuracao.setStatus(StatusApuracao.toStatusApuracaoEnum(dados.status()));
            dados.getResultadoAsLong().ifPresent(apuracao::setResultado);
        }

        // 4. Salva a apuração (seja ela nova ou atualizada)
        Apuracao apuracaoSalva = apuracaoRepository.save(apuracao);

        // 5. A lógica de re-cálculo de pontos é chamada em ambos os casos.
        _apurarResultadosEAtribuirPontos(apuracaoSalva.getProva().getId(), dados.tipoInscricao(), 6);

        return apuracaoSalva;
    }

    //Update
    public Apuracao update(Long id, DadosUpdateApuracoesRcd dados) {
        Apuracao apuracaoUpd = apuracaoRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Apuração não cadastrada. Id: " + id));

        if (dados.pontuacaoId() != null) {
            Pontuacao pontuacao = pontuacaoRepository.findById(dados.pontuacaoId())
                    .orElseThrow(() -> new ObjectNotFoundException("Pontuação não encontrada. Id: " + dados.pontuacaoId()));
            apuracaoUpd.setPontuacao(pontuacao);
            apuracaoUpd.setObservacao(pontuacao.getClassificacao()); // Faz sentido estar aqui dentro
        }

        // Só tenta setar o status SE ele foi enviado
        if (dados.status() != null) {
            apuracaoUpd.setStatus(StatusApuracao.toStatusApuracaoEnum(dados.status()));
        }

        dados.getResultadoAsLong().ifPresent(apuracaoUpd::setResultado);

        return apuracaoRepository.save(apuracaoUpd);
    }

    // Delete
    public void delete(Long id) {
        Apuracao apuracaoDel = apuracaoRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Apuração não cadastrada. Id: " + id));
        try {
            apuracaoRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ObjectNotFoundException(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    /**
     * Busca todas as apurações para uma prova, ordena por resultado,
     * calcula os pontos com base na posição e na pontuação definida,
     * e atualiza os registros no banco.
     *
     * @param provaId O ID da Prova a ser apurada.
     */
    private void _apurarResultadosEAtribuirPontos(Long provaId, Integer tipoInscricao, Integer qtdBalizas) {
        StatusApuracao statusApuracaoEnum;
        statusApuracaoEnum = StatusApuracao.toStatusApuracaoEnum(3); // Apurado
        List<Apuracao> apuracoes = apuracaoRepository.findByProvaIdAndStatusOrderByResultadoAsc(provaId, statusApuracaoEnum);

        List<DadosListApuracoesRcd> dtoList = apuracoes.stream()
                .map(apuracao -> DadosListApuracoesRcd.fromApuracao(apuracao, null))
                .collect(Collectors.toList());

        // Verifica a quantidade de inscricoes com status valido (inscrito,confirmado, presente)
        // E tipoInscricaoEnum igual a "Classificatória"(um)
        List<Integer> statusIn = List.of(0, 1, 2);
        StatusTipoInscricao tipoInscricaoEnum;
        tipoInscricaoEnum = StatusTipoInscricao.toStatusTipoInscricaoEnum(0); // fase classificacao

        int qtdInscricoes = inscricaoRepository.countByProvaIdAndStatusInAndTipoInscricao(
                provaId,
                statusIn,
                tipoInscricaoEnum
        );

        if (qtdInscricoes == 0) {
            tipoInscricaoEnum = StatusTipoInscricao.toStatusTipoInscricaoEnum(1); // fase semifinal
            qtdInscricoes = inscricaoRepository.countByProvaIdAndStatusInAndTipoInscricao(
                    provaId,
                    statusIn,
                    tipoInscricaoEnum
            );
            //System.err.println("Fez a busca semifinal : "  + qtdInscricoes);
        }

        if (qtdInscricoes == 0) {
            tipoInscricaoEnum = StatusTipoInscricao.toStatusTipoInscricaoEnum(2); // fase Final
            qtdInscricoes = inscricaoRepository.countByProvaIdAndStatusInAndTipoInscricao(
                    provaId,
                    statusIn,
                    tipoInscricaoEnum
            );
            //System.err.println("Fez a busca Final : " + qtdInscricoes);
        }

        // Pega quantidade de registros apurados  da prova
        int qtdApurados = dtoList.size();

        //System.err.println("Quantos foram apurados : " + qtdApurados);

        // Aqui procura saber se a apuração que esta sendo feita e uma fase de classificação.
        // pode ser que a apuração resulte em uma semifinal que é onde onde as inscricoes validas unidas aos registros
        // apurados divididos pela quantidade de balizas seja maior que 2 * qtdBalizas.  dtoList.size() / qtdBalizas

        // Se a  dtoList.size() / qtdBalizas for igual a 2, significa que a classificatoria foi uma semifinal
        // Se classificatoria menor que 2 foi uma final direta apura campeao e pontuacao.

        // Se encontrou registros(qtdInscricoes > 0) e porque esta na fase classificatoria.
        // se qtdInscricoes == qtdApurados é porque todas as inscrições validas ja foram apurados os resultados
        if (qtdInscricoes > 0 && (qtdInscricoes == qtdApurados)) {
            double qtdBaterias = (double) dtoList.size() / qtdBalizas;
            int regLidos = 0;

            StatusInscricao statusInscricaoEnum;

            // Encontra o maior número de série da fase que acabamos de apurar.
            int maximaSerieAnterior = apuracoes.stream()
                    .map(apuracao -> apuracao.getInscricao().getSerie())
                    .max(Integer::compareTo)
                    .orElse(0); // Se não houver séries, começa do 0.

            // A primeira série da nova fase será a próxima depois da maior da fase anterior.
            int serieInicialNovaFase = maximaSerieAnterior + 1;

            if (qtdBaterias > 2) {
                // vai ter fase semifinal,
                // ler  qtdBalizas * 2 e criar inscricções na fase semifinal para estes registros.

                statusApuracaoEnum = StatusApuracao.toStatusApuracaoEnum(0); // Classificado

                for (int i = 0; i < (dtoList.size()); i++) {
                    regLidos++;
                    Long apuracaoId = dtoList.get(i).id();
                    Long inscricaoId = dtoList.get(i).inscricaoId();

                    // Atualizar o status da inscricao para Apurado (6)
                    try {
                        statusInscricaoEnum = StatusInscricao.toStatusInscricaoEnum(6);
                        atualizarStatusInscricao(inscricaoId, statusInscricaoEnum);
                    } catch (Exception e) {
                        // Captura exceções que podem ocorrer durante a chamada do metodo
                        System.err.println("-> ERRO ao atualizar status para Inscrição ID: " + inscricaoId + " - " +
                                e.getMessage());
                        // Considere se deve parar o loop ou apenas logar e continuar
                    }

                    if (regLidos <= (qtdBalizas * 2)) {
                        // Atualizar o status da apuracao para Classificado (0)
                        try {
                            atualizarStatusApuracao(apuracaoId, statusApuracaoEnum, null);
                        } catch (Exception e) {
                            // Captura exceções que podem ocorrer durante a chamada do metodo
                            System.err.println("-> ERRO ao atualizar status para Apuração ID: " + inscricaoId + " - " +
                                    e.getMessage());
                            // Considere se deve parar o loop ou apenas logar e continuar
                        }

                        // Criar os registros de inscricoes na semifinal com os (qtdBalizas * 2) primeiros melhores tempos
                        // da fase de classificação.
                        try {

                            // A série é calculada com base na série inicial da nova fase.
                            // A divisão inteira (i / qtdBalizas) agrupa os atletas corretamente por bateria.
                            int serie = serieInicialNovaFase + (i / qtdBalizas);

                            List<Integer> lanePattern = getLaneSeedingPattern(qtdBalizas);
                            int patternIndex = i % qtdBalizas;
                            Integer baliza = null;

                            if (patternIndex < lanePattern.size()) {
                                baliza = lanePattern.get(patternIndex);
                            }

                            DadosInsertInscricoesRcd dadosNovaInscricao = new DadosInsertInscricoesRcd(
                                    1L,     // <<< PRECISA GARANTIR QUE ESTE CAMPO EXISTA NO SEU DTO de Listagem
                                    dtoList.get(i).provaId(),
                                    dtoList.get(i).atletaId(),
                                    serie,                     // série - Calculada para semifinal
                                    baliza,                     // baliza - Definida como null (requer seeding)
                                    0, // Status inicial para semifinal (Ex: 0 ou INSCRITO) <<< Use o Enum/valor correto (0 = INSCRITO?)
                                    1, // TIPO = SEMIFINAL (Enum/valor 2) <<< Use o Enum/valor correto
                                    "Classificado da fase anterior" // Observação opcional
                            );
                            inscricaoService.insert(dadosNovaInscricao);

                        } catch (Exception e) {
                            System.err.println("    -> ERRO ao criar inscrição para semifinal para Atleta ID: " + dtoList.get(i).atletaId() + " - " + e.getMessage());
                            // Considerar o que fazer em caso de falha aqui (continuar, parar, logar?)
                        }
                    } else {
                        // Atualizar o status da apuracao para Eliminado (4)
                        statusApuracaoEnum = StatusApuracao.toStatusApuracaoEnum(4); // Eliminado

                        try {
                            atualizarStatusApuracao(apuracaoId, statusApuracaoEnum, null);
                        } catch (Exception e) {
                            // Captura exceções que podem ocorrer durante a chamada do metodo
                            System.err.println("-> ERRO ao atualizar status para Apuração ID: " + inscricaoId + " - " +
                                    e.getMessage());
                            // Considere se deve parar o loop ou apenas logar e continuar
                        }
                    }
                }
            } else {
                if (qtdBaterias == 2) {
                    //Esta apurando a fase semifinal
                    System.err.println("Esta ma fase semifinal : ");

                    statusApuracaoEnum = StatusApuracao.toStatusApuracaoEnum(0); // Classificado

                    // COMEÇA AQUI.
                    for (int i = 0; i < (dtoList.size()); i++) {
                        regLidos++;
                        Long apuracaoId = dtoList.get(i).id();
                        Long inscricaoId = dtoList.get(i).inscricaoId();

                        // Atualizar o status da inscricao para Apurado (6)
                        try {
                            statusInscricaoEnum = StatusInscricao.toStatusInscricaoEnum(6);
                            atualizarStatusInscricao(inscricaoId, statusInscricaoEnum);
                        } catch (Exception e) {
                            // Captura exceções que podem ocorrer durante a chamada do metodo
                            System.err.println("-> ERRO ao atualizar status para Inscrição ID: " + inscricaoId + " - " +
                                    e.getMessage());
                            // Considere se deve parar o loop ou apenas logar e continuar
                        }

                        if (regLidos <= (qtdBalizas)) {
                            // Atualizar o status da apuracao para Classificado (0)
                            try {
                                atualizarStatusApuracao(apuracaoId, statusApuracaoEnum, null);
                            } catch (Exception e) {
                                // Captura exceções que podem ocorrer durante a chamada do metodo
                                System.err.println("-> ERRO ao atualizar status para Apuração ID: " + inscricaoId + " - " +
                                        e.getMessage());
                                // Considere se deve parar o loop ou apenas logar e continuar
                            }

                            // Criar os registros de inscricoes na final com os qtdBalizas primeiros melhores tempos
                            // da fase de semifianl.
                            try {

                                int serie = serieInicialNovaFase + (i / qtdBalizas);

                                List<Integer> lanePattern = getLaneSeedingPattern(qtdBalizas);
                                int patternIndex = i % qtdBalizas;
                                Integer baliza = null;

                                if (patternIndex < lanePattern.size()) {
                                    baliza = lanePattern.get(patternIndex);
                                }

                                DadosInsertInscricoesRcd dadosNovaInscricao = new DadosInsertInscricoesRcd(
                                        1L,     // <<< PRECISA GARANTIR QUE ESTE CAMPO EXISTA NO SEU DTO de Listagem
                                        dtoList.get(i).provaId(),
                                        dtoList.get(i).atletaId(),
                                        serie,                     // série - Calculada para semifinal
                                        baliza,                     // baliza - Definida como null (requer seeding)
                                        0, // Status inicial para semifinal (Ex: 0 ou INSCRITO) <<< Use o Enum/valor correto (0 = INSCRITO?)
                                        2, // TIPO = FINAL
                                        "Classificado para final" // Observação opcional
                                );
                                inscricaoService.insert(dadosNovaInscricao);

                            } catch (Exception e) {
                                System.err.println("    -> ERRO ao criar inscrição para semifinal para Atleta ID: " + dtoList.get(i).atletaId() + " - " + e.getMessage());
                                // Considerar o que fazer em caso de falha aqui (continuar, parar, logar?)
                            }
                        } else {
                            // Atualizar o status da apuracao para Eliminado (4)
                            statusApuracaoEnum = StatusApuracao.toStatusApuracaoEnum(4); // Eliminado

                            try {
                                atualizarStatusApuracao(apuracaoId, statusApuracaoEnum, null);
                            } catch (Exception e) {
                                // Captura exceções que podem ocorrer durante a chamada do metodo
                                System.err.println("-> ERRO ao atualizar status para Apuração ID: " + inscricaoId + " - " +
                                        e.getMessage());
                                // Considere se deve parar o loop ou apenas logar e continuar
                            }
                        }
                    }
                    // TERMINA AQUI.
                } else {
                    // Se for menor que 2 significa que a fase de classificação era tambem a semifinal.
                    // Ou as outras fases ja foram apuradas e esta na fase final
                    // Aqui apos apurar todos os resultados grava a pontuação(Se a etapa pontuar) e grava a colocação do atleta na prova
                    System.err.println("Processar a fase final : ");
                    Integer statusApuracao = 0;

                    Long pontuacaoId = Long.valueOf(regLidos);
                    for (int i = 0; i < (dtoList.size()); i++) {
                        pontuacaoId++;
                        Long apuracaoId = dtoList.get(i).id();
                        Long inscricaoId = dtoList.get(i).inscricaoId();

                        // Atualizar o status da inscricao para Apurado (6)
                        try {
                            statusInscricaoEnum = StatusInscricao.toStatusInscricaoEnum(6);
                            atualizarStatusInscricao(inscricaoId, statusInscricaoEnum);
                        } catch (Exception e) {
                            // Captura exceções que podem ocorrer durante a chamada do metodo
                            System.err.println("-> ERRO ao atualizar status para Inscrição ID: " + inscricaoId + " - " +
                                    e.getMessage());
                            // Considere se deve parar o loop ou apenas logar e continuar
                        }

                        if (pontuacaoId <= (qtdBalizas)) {
                            // Atualizar o status da apuracao de acordo com o resultado final

                            if (pontuacaoId == 1) {
                                statusApuracao = 2; // Vencedor
                            } else {
                                statusApuracao = 3; // Apurado
                            }

                            // Dados para chamar o update
                            String resultado = null;
                            Long PontuacaoId = pontuacaoId;
                            Integer status = statusApuracao;
                            String observacao = null;

                            DadosUpdateApuracoesRcd dados = new DadosUpdateApuracoesRcd(
                                    resultado,
                                    PontuacaoId,
                                    status,
                                    observacao
                            );

                            // Chama a função que faz update
                            try {
                                update(apuracaoId, dados);
                            } catch (Exception e) {
                                // Captura exceções que podem ocorrer durante a chamada do metodo
                                System.err.println("-> ERRO ao atualizar status para Apuração ID: " + inscricaoId + " - " +
                                        e.getMessage());
                                // Considere se deve parar o loop ou apenas logar e continuar
                            }
                        }
                    }
                }
            }
        }
    }

    @Transactional
    public boolean atualizarStatusInscricao(Long inscricaoId, StatusInscricao statusInscricaoEnum) {
        if (statusInscricaoEnum == null) { /* ... tratamento de nulo ... */ }
        try {
            // <<< A chamada passa o Enum diretamente >>>
            int linhasAfetadas = inscricaoRepository.updateStatusById(inscricaoId, statusInscricaoEnum);
            // ... resto da lógica de verificação de linhasAfetadas ...
            return linhasAfetadas > 0;
        } catch (Exception e) {
            System.err.println("Erro ao atualizar status via query nativa para ID " + inscricaoId + ": " + e.getMessage());
            // ... tratamento de erro ...
            throw new RuntimeException("Falha ao atualizar status da inscrição", e);
        }
    }

    @Transactional
    public boolean atualizarStatusApuracao(Long apuracaoId, StatusApuracao statusApuracaoEnum, Pontuacao pontuacao) {
        if (statusApuracaoEnum == null) { /* ... tratamento de nulo ... */ }
        try {
            // <<< A chamada passa o Enum diretamente >>>
            int linhasAfetadas = apuracaoRepository.updateStatusById(apuracaoId, statusApuracaoEnum);
            // ... resto da lógica de verificação de linhasAfetadas ...
            return linhasAfetadas > 0;
        } catch (Exception e) {
            System.err.println("Erro ao atualizar status via query nativa para ID " + apuracaoId + ": " + e.getMessage());
            // ... tratamento de erro ...
            throw new RuntimeException("Falha ao atualizar status da apuração", e);
        }
    }

    private List<Integer> getLaneSeedingPattern(int numberOfLanes) {
        List<Integer> fallbackPattern = new ArrayList<>();
        for (int i = 1; i <= numberOfLanes; i++) {
            fallbackPattern.add(i);
        }
        return Collections.unmodifiableList(fallbackPattern);
    }
}