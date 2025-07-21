package codiub.competicoes.api.DTO.pessoas;

import codiub.competicoes.api.entity.pessoas.Logradouros;

import java.util.Optional;

public record DadosLogradourosRcd(
        long id,
        String nome,
        String nomeReduzido,
        long distrito,
        String distritoNome

) {
    public DadosLogradourosRcd(Logradouros logradouros) {
        this(logradouros.getId(),
                logradouros.getNome(),
                logradouros.getNomeReduzido(),
                logradouros.getDistritos().getId(),
                logradouros.getDistritos().getNome());
    }

    public DadosLogradourosRcd(Optional<Logradouros> logradouros) {
        this(logradouros.get().getId(),
                logradouros.get().getNome(),
                logradouros.get().getNomeReduzido(),
                logradouros.get().getDistritos().getId(),
                logradouros.get().getDistritos().getNome());
    }
    public String getNome(){
                return nome.toUpperCase();
        }
    public String nomeAbreviado(){
        return nomeReduzido.toUpperCase();
    }
}
