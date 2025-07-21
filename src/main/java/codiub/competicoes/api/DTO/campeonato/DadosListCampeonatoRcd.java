package codiub.competicoes.api.DTO.campeonato;


import codiub.competicoes.api.entity.Campeonato;
import codiub.competicoes.api.entity.TiposModalidades;

import java.util.Optional;

public record DadosListCampeonatoRcd(Long id, String empresa, String nome, String descricao, Long modalidadeId, String modalidadeNome){

    public DadosListCampeonatoRcd(Campeonato campeonato){
        this(campeonato.getId(),
                campeonato.getEmpresa().getNome(),
                campeonato.getNome(),
                campeonato.getDescricao(),
                campeonato.getModalidade().getId(),
                campeonato.getModalidade().getNome());
    }
    public DadosListCampeonatoRcd(Optional<Campeonato> campeonato) {
        this(campeonato.get().getId(),
                campeonato.get().getEmpresa().getNome(),
                campeonato.get().getNome(),
                campeonato.get().getDescricao(),
                campeonato.get().getModalidade().getId(),
                campeonato.get().getModalidade().getNome());
    }

}
