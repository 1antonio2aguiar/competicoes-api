package codiub.competicoes.api.DTO.locaisCompeticoes;


import codiub.competicoes.api.entity.LocaisCompeticoes;
import codiub.competicoes.api.entity.TiposModalidades;

import java.util.Optional;

public record DadosPageLocaisCompeticoesRcd(Long id, String nome, String endereco){

    public DadosPageLocaisCompeticoesRcd(LocaisCompeticoes locaisCompeticoes){
        this(locaisCompeticoes.getId(),
                locaisCompeticoes.getNome(),
                locaisCompeticoes.getEndereco());
    }
    public DadosPageLocaisCompeticoesRcd(Optional<LocaisCompeticoes> locaisCompeticoes) {
        this(locaisCompeticoes.get().getId(),
            locaisCompeticoes.get().getNome(),
            locaisCompeticoes.get().getEndereco());
    }
}
