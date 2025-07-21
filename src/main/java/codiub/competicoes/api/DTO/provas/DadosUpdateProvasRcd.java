package codiub.competicoes.api.DTO.provas;

import codiub.competicoes.api.utils.TimeConversionUtils;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Optional;

public record DadosUpdateProvasRcd(
    Long categoriaId,
    Long tipoNadoId,
    Integer distancia,
    String genero,
    String revezamento,
    String medley,
    String indiceTecnico,
    String record,
    String tipoPiscina,
    String observacao ) {
    public String getObservacao(){
        if (observacao != null) {
            return observacao.toUpperCase();
        } else {
            return "";
        }
    }
    public Optional<Long> getIndiceTecnicoAsLong() {
        return TimeConversionUtils.convertStringToLong(this.indiceTecnico);
    }
    public Optional<Long> getRecordAsLong() {
        return TimeConversionUtils.convertStringToLong(this.record);
    }

}
