package codiub.competicoes.api.DTO.tipoModalidade;


import codiub.competicoes.api.entity.TiposModalidades;

import java.util.List;
import java.util.Optional;

public record DadosListTipoModalidadeRcd (Long id, String nome){

    public  DadosListTipoModalidadeRcd(TiposModalidades tipoModalidade){
        this(tipoModalidade.getId(),
                tipoModalidade.getNome());
    }
}
