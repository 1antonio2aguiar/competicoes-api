package codiub.competicoes.api.DTO.pessoas;

import codiub.competicoes.api.entity.pessoas.Bairros;
import codiub.competicoes.api.entity.pessoas.Cidades;

import java.util.Optional;

public record DadosBairrosRcd(
        long id,
        String nome,
        String nomeAbreviado,
        long distrito,
        String distritoNome

) {
    public DadosBairrosRcd(Bairros bairros) {
        this(bairros.getId(),
                bairros.getNome(),
                bairros.getNomeAbreviado(),
                bairros.getDistritos().getId(),
                bairros.getDistritos().getNome());
    }

    public DadosBairrosRcd(Optional<Bairros> bairros) {
        this(bairros.get().getId(),
                bairros.get().getNome(),
                bairros.get().getNomeAbreviado(),
                bairros.get().getDistritos().getId(),
                bairros.get().getDistritos().getNome());
    }
    public String getNome(){
                return nome.toUpperCase();
        }
    public String nomeAbreviado(){
        return nomeAbreviado.toUpperCase();
    }
}
