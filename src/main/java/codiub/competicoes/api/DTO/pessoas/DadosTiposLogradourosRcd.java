package codiub.competicoes.api.DTO.pessoas;

import codiub.competicoes.api.entity.pessoas.TiposLogradouros;

import java.util.Optional;

public record DadosTiposLogradourosRcd(
        Long id,
        String descricao,
        String sigla
) {
    public DadosTiposLogradourosRcd(TiposLogradouros tiposLogradouros) {
        this(tiposLogradouros.getId(),
                tiposLogradouros.getDescricao(),
                tiposLogradouros.getSigla());
    }

    public DadosTiposLogradourosRcd(Optional<TiposLogradouros> tiposLogradouros) {
        this(tiposLogradouros.get().getId(),
                tiposLogradouros.get().getDescricao(),
                tiposLogradouros.get().getSigla());
    }
    public String getDescricao(){
                return descricao.toUpperCase();
        }
}
