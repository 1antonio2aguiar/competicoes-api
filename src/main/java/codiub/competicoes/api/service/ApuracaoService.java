package codiub.competicoes.api.service;

import codiub.competicoes.api.DTO.apuracao.DadosInsertApuracoesRcd;
import codiub.competicoes.api.DTO.apuracao.DadosListApuracaoAndInscricaoRcd;
import codiub.competicoes.api.DTO.apuracao.DadosListApuracoesRcd;
import codiub.competicoes.api.DTO.apuracao.DadosUpdateApuracoesRcd;
import codiub.competicoes.api.DTO.inscricoes.DadosInsertInscricoesRcd;
import codiub.competicoes.api.entity.*;
import codiub.competicoes.api.entity.enuns.StatusApuracao;
import codiub.competicoes.api.entity.enuns.StatusInscricao;
import codiub.competicoes.api.entity.enuns.StatusTipoInscricao;
import codiub.competicoes.api.filter.ApuracaoFilter;
import codiub.competicoes.api.repository.*;
import codiub.competicoes.api.repository.apuracao.custon.ApuracaoCustonRepository;
import codiub.competicoes.api.service.exceptions.DatabaseException;
import codiub.competicoes.api.service.exceptions.ObjectNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ApuracaoService {
    @Autowired private ApuracaoRepository apuracaoRepository;
    @Autowired private EmpresaRepository empresaRepository;
    @Autowired private ProvaRepository provaRepository;
    @Autowired private InscricaoRepository inscricaoRepository;
    @Autowired private InscricaoService inscricaoService;
    @Autowired private AtletaRepository atletaRepository;
    @Autowired private PontuacaoRepository pontuacaoRepository;
    @Autowired private ApuracaoCustonRepository apuracaoCustonRepository;
    public ApuracaoService(ApuracaoRepository apuracaoRepository) {
        this.apuracaoRepository = apuracaoRepository;
    }

    //Metodo filtrar
    public Page<DadosListApuracoesRcd> pesquisar(ApuracaoFilter filter, Pageable pageable) {
        Page<Apuracao> apuracaoPage = apuracaoRepository.filtrar(filter, pageable);

        // Mapeia a lista de Apuracoes para uma lista de DadosListApuracoesRcd usando o método de fábrica
        List<DadosListApuracoesRcd> apuracaoDTOList = apuracaoPage.getContent().stream()
                .map(DadosListApuracoesRcd::fromApuracao)
                .collect(Collectors.toList());

        // Cria um novo Page<DadosListApuracoesRcd> com os dados mapeados
        return new PageImpl<>(apuracaoDTOList, pageable, apuracaoPage.getTotalElements());
    }

    // Apuracoes por id
    public DadosListApuracoesRcd findById(Long id) {
        Optional<Apuracao> apuracaoOptional = apuracaoRepository.findById(id);
        return apuracaoOptional.map(DadosListApuracoesRcd::fromApuracao).orElse(null);
    }

    public Page<DadosListApuracoesRcd> findall(Pageable paginacao) {
        Page<Apuracao> apuracaoPage = apuracaoRepository.findAll(paginacao);
        List<DadosListApuracoesRcd> apuracaoDTOList = apuracaoPage.getContent().stream()
                .map(DadosListApuracoesRcd::fromApuracao) // Usa o método de fábrica
                .collect(Collectors.toList());
        return new PageImpl<>(apuracaoDTOList, paginacao, apuracaoPage.getTotalElements());
    }

    public Page<DadosListApuracaoAndInscricaoRcd> apuracaoAndInscricao(ApuracaoFilter filter, Pageable pageable) {
        // Recupera  inscricao e apuracao.
        Page<DadosListApuracaoAndInscricaoRcd> apuracaoPage = apuracaoCustonRepository.apuracaoAndInscricao(filter, pageable);

        // Cria um novo Page<DadosAtletasRcd> com os dados mapeados
        return (apuracaoPage);
    }

    //Insert
    public Apuracao insert(DadosInsertApuracoesRcd dados){
        Apuracao apuracao = new Apuracao();

        BeanUtils.copyProperties(dados, apuracao, "id");

        //Busco a empresa
        if(dados.empresaId() != null && dados.empresaId() != 0) {
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

        apuracao = apuracaoRepository.save(apuracao);

        _apurarResultadosEAtribuirPontos(apuracao.getProva().getId(),dados.tipoInscricao(),6);

        return (apuracao);
    }

    //Update
     public Apuracao update(Long id, DadosUpdateApuracoesRcd dados){

        Apuracao apuracaoUpd = apuracaoRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Apuração não cadastrada. Id: " + id));

        //Busco pontuacao
        Pontuacao pontuacao = pontuacaoRepository.findById(dados.pontuacaoId()).get();
        apuracaoUpd.setPontuacao(pontuacao);

        BeanUtils.copyProperties(dados, apuracaoUpd, "id");

        dados.getResultadoAsLong().ifPresent(apuracaoUpd::setResultado);
         apuracaoUpd.setObservacao(pontuacao.getClassificacao());
         apuracaoUpd.setStatus(StatusApuracao.toStatusApuracaoEnum(dados.status()));
        return apuracaoRepository.save(apuracaoUpd);
    }

    // Delete
    public void delete(Long id){
        Apuracao apuracaoDel = apuracaoRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Apuração não cadastrada. Id: " + id));
        try {
            apuracaoRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ObjectNotFoundException(id);
        } catch (DataIntegrityViolationException e){
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
        List<Apuracao> apuracoes = apuracaoRepository.findByProvaIdAndStatusOrderByResultadoAsc(provaId,statusApuracaoEnum);

        List<DadosListApuracoesRcd> dtoList = apuracoes.stream()
                .map(DadosListApuracoesRcd::fromApuracao)
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

        if ( qtdInscricoes == 0) {
            tipoInscricaoEnum = StatusTipoInscricao.toStatusTipoInscricaoEnum(1); // fase semifinal
            qtdInscricoes = inscricaoRepository.countByProvaIdAndStatusInAndTipoInscricao(
                    provaId,
                    statusIn,
                    tipoInscricaoEnum
            );
            System.err.println("Fez a busca semifinal : "  + qtdInscricoes);
        }

        if ( qtdInscricoes == 0) {
            tipoInscricaoEnum = StatusTipoInscricao.toStatusTipoInscricaoEnum(2); // fase Final
            qtdInscricoes = inscricaoRepository.countByProvaIdAndStatusInAndTipoInscricao(
                    provaId,
                    statusIn,
                    tipoInscricaoEnum
            );
            System.err.println("Fez a busca Final : "  + qtdInscricoes);
        }

        // Pega quantidade de registros apurados  da prova
        int qtdApurados = dtoList.size();

        System.err.println("Quantos foram apurados : "  + qtdApurados);

        // Aqui procura saber se a apuração que esta sendo feita e uma fase de classificação.
        // pode ser que a apuração resulte em uma semifinal que é onde onde as inscricoes validas unidas aos registros
        // apurados divididos pela quantidade de balizas seja maior que 2 * qtdBalizas.  dtoList.size() / qtdBalizas

        // Se a  dtoList.size() / qtdBalizas for igual a 2, significa que a classificatoria foi uma semifinal
        // Se classificatoria menor que 2 foi uma final direta apura campeao e pontuacao.

        // Se encontrou registros(qtdInscricoes > 0) e porque esta na fase classificatoria.
        // se qtdInscricoes == qtdApurados é porque todas as inscrições validas ja foram apurados os resultados
        if ( qtdInscricoes > 0 &&  (qtdInscricoes == qtdApurados)) {
            double qtdBaterias =  (double)  dtoList.size() / qtdBalizas;
            int regLidos = 0;

            StatusInscricao statusInscricaoEnum;

            System.err.println("Quanto deu  qtdBaterias: "  + qtdBaterias);

            if (qtdBaterias > 2){
                // vai ter fase semifinal,
                // ler  qtdBalizas * 2 e criar inscricções na fase semifinal para estes registros.

                statusApuracaoEnum = StatusApuracao.toStatusApuracaoEnum(0); // Classificado

                for (int i = 0; i < (dtoList.size()); i++) {
                    regLidos ++;
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

                    if ( regLidos <= (qtdBalizas * 2 )) {
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

                            Integer serie = (regLidos <= qtdBalizas) ? 1 : 2;

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
                if (qtdBaterias == 2){
                    //Esta apurando a fase semifinal
                    System.err.println("Esta ma fase semifinal : "  );

                    statusApuracaoEnum = StatusApuracao.toStatusApuracaoEnum(0); // Classificado

                    // COMEÇA AQUI.
                    for (int i = 0; i < (dtoList.size()); i++) {
                        regLidos ++;
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

                        if ( regLidos <= (qtdBalizas )) {
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

                                Integer serie = (regLidos <= qtdBalizas) ? 1 : 2;

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
                    System.err.println("Processar a fase final : "  );
                    Integer statusApuracao = 0;

                    Long pontuacaoId = Long.valueOf(regLidos);
                    for (int i = 0; i < (dtoList.size()); i++) {
                        pontuacaoId ++;
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

                        if ( pontuacaoId <= (qtdBalizas )) {
                            // Atualizar o status da apuracao de acordo com o resultado final

                            if (pontuacaoId == 1) {
                                statusApuracao = 2; // Vencedor
                            } else {
                                statusApuracao = 3; // Apurado
                            }

                            // Dados para chamar o update
                            String resultado = null ;
                            Long PontuacaoId = pontuacaoId;
                            Integer status = statusApuracao;
                            String observacao = null ;

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