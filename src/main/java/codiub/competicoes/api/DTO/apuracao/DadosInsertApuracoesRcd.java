package codiub.competicoes.api.DTO.apuracao;

import codiub.competicoes.api.utils.TimeConversionUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Optional;

public record DadosInsertApuracoesRcd(
        Long empresaId,
        Long provaId,
        Long inscricaoId,
        Long atletaId,
        Long pontuacaoId,
        String resultado,
        Integer status,
        Integer tipoInscricao,
        String observacao
) {
        public String getObservacao(){
                if (observacao != null) {
                        return observacao.toUpperCase();
                } else {
                        return "";
                }
        }

        public Optional<Long> getResultadoAsLong() {
                return TimeConversionUtils.convertStringToLong(this.resultado);
        }
}
