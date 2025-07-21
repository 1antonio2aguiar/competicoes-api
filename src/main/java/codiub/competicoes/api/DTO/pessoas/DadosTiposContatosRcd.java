package codiub.competicoes.api.DTO.pessoas;

import codiub.competicoes.api.entity.pessoas.TiposContatos;

import java.util.Optional;

public record DadosTiposContatosRcd(
        Long id,
        String nome
) {
    public DadosTiposContatosRcd(TiposContatos tiposContatos) {
        this(tiposContatos.getId(),
                tiposContatos.getNome());
    }

    public DadosTiposContatosRcd(Optional<TiposContatos> tiposContatos) {
        this(tiposContatos.get().getId(),
                tiposContatos.get().getNome());
    }
    public String getNome(){
                return nome.toUpperCase();
        }
}
