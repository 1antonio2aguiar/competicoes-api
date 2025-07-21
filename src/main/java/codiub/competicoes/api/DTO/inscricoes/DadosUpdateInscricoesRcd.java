package codiub.competicoes.api.DTO.inscricoes;

import java.text.SimpleDateFormat;
import java.util.Optional;

public record DadosUpdateInscricoesRcd(
    Integer baliza,
    Integer serie,
    Integer status,
    Integer statusTipoInscricao,
    String observacao ) {
    public String getObservacao(){
        if (observacao != null) {
            return observacao.toUpperCase();
        } else {
            return "";
        }
    }
}
