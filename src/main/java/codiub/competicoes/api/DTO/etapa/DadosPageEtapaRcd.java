package codiub.competicoes.api.DTO.etapa;


import codiub.competicoes.api.entity.Etapa;
import codiub.competicoes.api.entity.LocaisCompeticoes;

import java.util.Date;
import java.util.Optional;

public record DadosPageEtapaRcd(Long id, String empresa, String campeonato, String local, String nome, Date dataEtapa,
                                Date dataInscricao, String pontua, String acumula){

    public DadosPageEtapaRcd(Etapa etapa){
        this(etapa.getId(),
        etapa.getEmpresa().getNome(),
        etapa.getCampeonato().getNome(),
        etapa.getLocalCompeticao().getNome(),
        etapa.getNome(),
        etapa.getDataEtapa(),
        etapa.getDataInscricao(),
        etapa.getPontua(),
        etapa.getAcumula());
    }
    public DadosPageEtapaRcd(Optional<Etapa> etapa) {
        this(etapa.get().getId(),
        etapa.get().getEmpresa().getNome(),
        etapa.get().getCampeonato().getNome(),
        etapa.get().getLocalCompeticao().getNome(),
        etapa.get().getNome(),
        etapa.get().getDataEtapa(),
        etapa.get().getDataInscricao(),
        etapa.get().getPontua(),
        etapa.get().getAcumula());
    }
}
