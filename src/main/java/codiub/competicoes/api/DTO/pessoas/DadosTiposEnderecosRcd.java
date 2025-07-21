package codiub.competicoes.api.DTO.pessoas;

import codiub.competicoes.api.entity.pessoas.TiposContatos;
import codiub.competicoes.api.entity.pessoas.TiposEnderecos;

import java.util.Optional;

public record DadosTiposEnderecosRcd(
        Long id,
        String nome
) {
    public DadosTiposEnderecosRcd(TiposEnderecos tiposEnderecos) {
        this(tiposEnderecos.getId(),
                tiposEnderecos.getNome());
    }

    public DadosTiposEnderecosRcd(Optional<TiposEnderecos> tiposEnderecos) {
        this(tiposEnderecos.get().getId(),
                tiposEnderecos.get().getNome());
    }
    public String getNome(){
                return nome.toUpperCase();
        }
}
