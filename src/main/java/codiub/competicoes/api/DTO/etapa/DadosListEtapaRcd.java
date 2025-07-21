package codiub.competicoes.api.DTO.etapa;


import codiub.competicoes.api.entity.Etapa;
import codiub.competicoes.api.entity.LocaisCompeticoes;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Date;

public record DadosListEtapaRcd(Long id, Long empresa, String nome , long campeonato,
    long localCompeticao,
    Date dataEtapa,Date dataInscricao, String pontua, String acumula,
    String descricao ){

    public DadosListEtapaRcd(Etapa etapa){
        this(etapa.getId(),
        etapa.getEmpresa().getId(),
        etapa.getNome(),
        etapa.getCampeonato().getId(),
        etapa.getLocalCompeticao().getId(),
        etapa.getDataEtapa(),
        etapa.getDataInscricao(),
        etapa.getPontua(),
        etapa.getAcumula(),
        etapa.getDescricao()
        );
    }
}
