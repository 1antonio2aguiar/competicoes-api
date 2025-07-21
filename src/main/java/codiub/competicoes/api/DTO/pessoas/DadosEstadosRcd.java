package codiub.competicoes.api.DTO.pessoas;

import codiub.competicoes.api.entity.pessoas.Estados;

import java.util.Optional;

public record DadosEstadosRcd(
        long id,
        String nome,
       long pais,
        String paisNome

) {
    public DadosEstadosRcd(Estados estados) {
        this(estados.getId(),
                estados.getNome(),
                estados.getPaises().getId(),
                estados.getPaises().getNome());
    }

    public DadosEstadosRcd(Optional<Estados> estados) {
        this(estados.get().getId(),
                estados.get().getNome(),
                estados.get().getPaises().getId(),
                estados.get().getPaises().getNome());
    }
    public String getNome(){
                return nome.toUpperCase();
        }
}
