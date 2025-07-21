package codiub.competicoes.api.DTO.parametro;


import codiub.competicoes.api.DTO.provas.DadosListProvasRcd;
import codiub.competicoes.api.entity.Etapa;
import codiub.competicoes.api.entity.Parametro;
import codiub.competicoes.api.entity.Prova;

import java.util.Date;

import static codiub.competicoes.api.entity.Prova_.etapa;

public record DadosListParametroRcd(Long id, Integer numero,
    Long camponatoId, String campeonatoNome,
    Long etapaId, String estapaNome,
    String descricao ){

    public DadosListParametroRcd(Parametro parametro){
        this(parametro.getId(),
        parametro.getNumero(),
        parametro.getEtapa().getCampeonato().getId(),
        parametro.getEtapa().getCampeonato().getNome(),
        parametro.getEtapa().getId(),
        parametro.getEtapa().getNome(),
        parametro.getDescricao()
        );
    }

    public static DadosListParametroRcd fromParametro(Parametro parametro) {
        return new DadosListParametroRcd(
                parametro.getId(),
                parametro.getNumero(),
                parametro.getEtapa().getCampeonato().getId(),
                parametro.getEtapa().getCampeonato().getNome(),
                parametro.getEtapa().getId(),
                parametro.getEtapa().getNome(),
                parametro.getDescricao());
    }
}
