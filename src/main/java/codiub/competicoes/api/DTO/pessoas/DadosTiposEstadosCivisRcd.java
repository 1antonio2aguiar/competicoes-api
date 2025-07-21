package codiub.competicoes.api.DTO.pessoas;

import codiub.competicoes.api.entity.pessoas.TiposEstadosCivis;

import java.util.Optional;

public record DadosTiposEstadosCivisRcd(
        Long id,
        String nome
) {
    public DadosTiposEstadosCivisRcd(TiposEstadosCivis tiposEstadosCivis) {
        this(tiposEstadosCivis.getId(),
                tiposEstadosCivis.getNome());
    }

    public DadosTiposEstadosCivisRcd(Optional<TiposEstadosCivis> tiposEstadosCivis) {
        this(tiposEstadosCivis.get().getId(),
                tiposEstadosCivis.get().getNome());
    }
    public String getNome(){
                return nome.toUpperCase();
        }
}
