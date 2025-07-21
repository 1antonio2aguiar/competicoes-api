package codiub.competicoes.api.DTO.pessoas;

import codiub.competicoes.api.entity.pessoas.TiposDocumentos;

import java.util.Optional;

public record DadosTiposDocumentosRcd(
        Long id,
        String nome
) {
    public DadosTiposDocumentosRcd(TiposDocumentos tiposDocumentos) {
        this(tiposDocumentos.getId(),
                tiposDocumentos.getNome());
    }

    public DadosTiposDocumentosRcd(Optional<TiposDocumentos> tiposDocumentos) {
        this(tiposDocumentos.get().getId(),
                tiposDocumentos.get().getNome());
    }
    public String getNome(){
                return nome.toUpperCase();
        }
}
