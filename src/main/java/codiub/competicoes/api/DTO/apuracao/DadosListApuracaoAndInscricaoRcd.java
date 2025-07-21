package codiub.competicoes.api.DTO.apuracao;

import codiub.competicoes.api.entity.Apuracao;
import codiub.competicoes.api.entity.Prova;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

public record DadosListApuracaoAndInscricaoRcd(
        Long inscricaoId,
        Long apuracaoId,
        Long provaId,
        String equipeNome,
        Long atletaId,
        String atletaNome,
        Integer serie,
        Integer baliza,
        String resultado,
        Integer tipoInscricao
) {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss:SSS");

    // Construtor auxiliar que recebe Long para resultadoMillis e formata
    public DadosListApuracaoAndInscricaoRcd(
            Long inscricaoId,
            Long apuracaoId,
            Long provaId,
            String equipeNome,
            Long atletaId,
            String atletaNome,
            Integer serie,
            Integer baliza,
            Long resultadoMillis,
            Integer tipoInscricao
    ) {
        this(
                inscricaoId,
                apuracaoId,
                provaId,
                equipeNome,
                atletaId,
                atletaNome,
                serie,
                baliza,
                formatTime(resultadoMillis),
                tipoInscricao
        );
    }

    // Método para formatar o tempo (mantido como estava)
    private static String formatTime(Long timeInMillis) {
        if (timeInMillis == null) {
            return null; // Ou retorne uma string vazia, se preferir ""
        }

        if (timeInMillis == 0) {
            return "00:00:00:000";
        }

        return sdf.format(new Date(timeInMillis));
    }

    // Método para criar o DTO a partir de uma entidade Apuracoes
    public static DadosListApuracaoAndInscricaoRcd fromApuracao(Apuracao apuracao) {
        return new DadosListApuracaoAndInscricaoRcd(
                apuracao.getInscricao().getId(),
                apuracao.getId(),
                apuracao.getProva().getId(),
                apuracao.getAtleta().getEquipe().getNome(),
                apuracao.getAtleta().getId(),
                apuracao.getAtleta().getPessoa().getNome(),
                apuracao.getInscricao().getSerie(),
                apuracao.getInscricao().getBaliza(),
                apuracao.getResultado(),
                apuracao.getInscricao().getTipoInscricao().getCodigo()
        );
    }

    // Método para lidar com o Optional<Apuracao>
    public static DadosListApuracaoAndInscricaoRcd fromOptionalApuracao(Optional<Apuracao> apuracaoOptional) {
        return apuracaoOptional.map(DadosListApuracaoAndInscricaoRcd::fromApuracao).orElse(null);
    }
}