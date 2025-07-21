package codiub.competicoes.api.DTO.apuracao;

import codiub.competicoes.api.entity.Apuracao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

public record DadosListApuracoesRcd(
        Long id,
        Long inscricaoId,
        Long provaId,
        Long atletaId,
        Integer status,
        String statusDescricao,
        String resultado, // String formatada
        String observacao,
        Long pontuacaoId
) {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss:SSS");

    // Construtor "completo" (gerado pelo record)
    public DadosListApuracoesRcd(Long id, Long inscricaoId, Long provaId, Long atletaId,
                                 Integer status, String statusDescricao, String resultado, String observacao,
        Long pontuacaoId) {
        this.id = id;
        this.inscricaoId = inscricaoId;
        this.provaId = provaId;
        this.atletaId = atletaId;
        this.status = status;
        this.statusDescricao = statusDescricao;
        this.resultado = resultado;
        this.observacao = observacao;
        this.pontuacaoId = pontuacaoId;
    }

    // Construtor auxiliar que recebe Long para resultadoMillis e formata
    public DadosListApuracoesRcd(
            Long id,
            Long inscricaoId,
            Long provaId,
            Long atletaId,
            Integer status,
            String statusDescricao,
            Long resultadoMillis, // Recebe Long
            String observacao,
            Long pontuacaoId
    ) {
        this(
                id,
                inscricaoId,
                provaId,
                atletaId,
                status,
                statusDescricao,
                formatTime(resultadoMillis), // Formata resultado
                observacao,
                pontuacaoId
        );
    }

    // Método para formatar o tempo (mantido como estava)
    private static String formatTime(Long timeInMillis) {
        if (timeInMillis == null) {
            return null; // Ou retorne uma string vazia, se preferir ""
        }
        return sdf.format(new Date(timeInMillis));
    }

    // Método para criar o DTO a partir de uma entidade Apuracoes
    public static DadosListApuracoesRcd fromApuracao(Apuracao apuracao) {
        return new DadosListApuracoesRcd(
                apuracao.getId(),
                apuracao.getInscricao().getId(),
                apuracao.getProva().getId(),
                apuracao.getAtleta().getId(),
                apuracao.getStatus().getCodigo(),
                apuracao.getStatus().getDescricao(),
                apuracao.getResultado(), // Passa o Long para o construtor formatar
                apuracao.getObservacao(),
                apuracao.getPontuacao().getId()
        );
    }

    // Método para lidar com o Optional<Apuracao>
    public static DadosListApuracoesRcd fromOptionalApuracao(Optional<Apuracao> apuracaoOptional) {
        return apuracaoOptional.map(DadosListApuracoesRcd::fromApuracao).orElse(null);
    }
}