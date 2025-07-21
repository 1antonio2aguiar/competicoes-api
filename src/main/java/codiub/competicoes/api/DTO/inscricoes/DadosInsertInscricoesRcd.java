package codiub.competicoes.api.DTO.inscricoes;

import java.text.SimpleDateFormat;
import java.util.Optional;

public record DadosInsertInscricoesRcd(
        Long empresaId,
        Long provaId,
        Long atletaId,
        Integer serie,
        Integer baliza,
        Integer status,
        Integer statusTipoInscricao,
        String observacao
) {
        public String getObservacao(){
                if (observacao != null) {
                        return observacao.toUpperCase();
                } else {
                        return "";
                }
        }
}
