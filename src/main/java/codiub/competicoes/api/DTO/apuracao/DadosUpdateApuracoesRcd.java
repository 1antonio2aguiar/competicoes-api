package codiub.competicoes.api.DTO.apuracao;

import codiub.competicoes.api.utils.TimeConversionUtils;

import java.text.SimpleDateFormat;
import java.util.Optional;

public record DadosUpdateApuracoesRcd(
    String resultado,
    Long pontuacaoId,
    Integer status,
    String observacao ) {
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
