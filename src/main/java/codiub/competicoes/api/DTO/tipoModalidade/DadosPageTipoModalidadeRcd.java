package codiub.competicoes.api.DTO.tipoModalidade;


import codiub.competicoes.api.entity.TiposModalidades;

import java.util.Optional;

public record DadosPageTipoModalidadeRcd(Long id, String nome, String descricao){

    public DadosPageTipoModalidadeRcd(TiposModalidades tipoModalidade){
        this(tipoModalidade.getId(),
                tipoModalidade.getNome(),
                tipoModalidade.getDescricao());
    }
    public DadosPageTipoModalidadeRcd(Optional<TiposModalidades> tipoModalidade) {
        this(tipoModalidade.get().getId(),
                tipoModalidade.get().getNome(),
                tipoModalidade.get().getDescricao());
    }

}
