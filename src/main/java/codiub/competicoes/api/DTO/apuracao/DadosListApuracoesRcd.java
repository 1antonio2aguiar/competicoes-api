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
        String atletaNome, // << CAMPO ADICIONADO
        Integer status,
        String statusDescricao,
        String resultado, // String formatada
        String observacao,
        Long pontuacaoId
) {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss:SSS");

    // O construtor canônico (com todos os campos) é gerado automaticamente pelo record.
    // NÃO precisamos mais dos construtores manuais. Vamos removê-los para simplificar.

    // Método para formatar o tempo (continua igual)
    private static String formatTime(Long timeInMillis) {
        if (timeInMillis == null) { return null; }
        return sdf.format(new Date(timeInMillis));
    }

    // 2. MODIFIQUE O MÉTODO DE FÁBRICA
    // Ele agora recebe a Apuracao, o atletaNome, e faz a formatação do tempo aqui dentro.
    public static DadosListApuracoesRcd fromApuracao(Apuracao apuracao, String atletaNome) {
        return new DadosListApuracoesRcd(
                apuracao.getId(),
                apuracao.getInscricao().getId(),
                apuracao.getProva().getId(),
                apuracao.getAtleta().getId(),
                atletaNome, // << USA O NOVO PARÂMETRO
                apuracao.getStatus().getCodigo(),
                apuracao.getStatus().getDescricao(),
                formatTime(apuracao.getResultado()), // Formata o resultado aqui
                apuracao.getObservacao(),
                apuracao.getPontuacao() != null ? apuracao.getPontuacao().getId() : null // Proteção contra nulo
        );
    }

}