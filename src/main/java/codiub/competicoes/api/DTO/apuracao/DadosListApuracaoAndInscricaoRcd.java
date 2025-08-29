package codiub.competicoes.api.DTO.apuracao;

import java.text.SimpleDateFormat;
import java.util.Date;

public record DadosListApuracaoAndInscricaoRcd(
        Long inscricaoId,
        Long apuracaoId,
        Long provaId,
        String equipeNome,
        Long atletaId,
        String atletaNome, // << CAMPO ADICIONADO
        Integer serie,
        Integer baliza,
        String resultado,
        Integer tipoInscricao
) {
    // 2. ADICIONE O MÉTODO 'formatTime' AQUI DENTRO
    private static final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss:SSS");

    private static String formatTime(Long timeInMillis) {
        if (timeInMillis == null) {
            return null;
        }
        if (timeInMillis == 0) {
            return "00:00:00:000";
        }
        return sdf.format(new Date(timeInMillis));
    }

    // 3. CRIE UM NOVO CONSTRUTOR AUXILIAR
    // Este construtor será usado pelo seu TupleTransformer na query nativa.
    // Ele recebe o resultado como um Long e chama o construtor principal, formatando a data.
    public DadosListApuracaoAndInscricaoRcd(
            Long inscricaoId, Long apuracaoId, Long provaId, String equipeNome,
            Long atletaId, Integer serie, Integer baliza, Long resultadoMillis, Integer tipoInscricao
    ) {
        this( // Chama o construtor principal (canônico)
                inscricaoId, apuracaoId, provaId, equipeNome, atletaId,
                null, // atletaNome começa nulo, será preenchido depois pelo service
                serie, baliza, formatTime(resultadoMillis), // <<<< USA O MÉTODO AQUI
                tipoInscricao
        );
    }
}