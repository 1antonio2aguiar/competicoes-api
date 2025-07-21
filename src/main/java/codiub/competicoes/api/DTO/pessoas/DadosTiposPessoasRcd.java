package codiub.competicoes.api.DTO.pessoas;

import codiub.competicoes.api.entity.pessoas.TiposPessoas;

import java.util.Optional;

public record DadosTiposPessoasRcd(
        Long id,
        String nome
) {
    public DadosTiposPessoasRcd(TiposPessoas tiposPessoas) {
        this(tiposPessoas.getId(),
                tiposPessoas.getNome());
    }

    public DadosTiposPessoasRcd(Optional<TiposPessoas> tiposPessoas) {
        this(tiposPessoas.get().getId(),
                tiposPessoas.get().getNome());
    }
    public String getNome(){
                return nome.toUpperCase();
        }
}
