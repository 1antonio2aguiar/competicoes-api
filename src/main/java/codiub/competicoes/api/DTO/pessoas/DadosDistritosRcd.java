package codiub.competicoes.api.DTO.pessoas;

import codiub.competicoes.api.entity.pessoas.Cidades;
import codiub.competicoes.api.entity.pessoas.Distritos;

import java.util.Optional;

public record DadosDistritosRcd(
        long id,
        String nome,
        long cidade,
        String cidadeNome

) {
    public DadosDistritosRcd(Distritos distritos) {
        this(distritos.getId(),
                distritos.getNome(),
                distritos.getCidades().getId(),
                distritos.getCidades().getNome());
    }

    public DadosDistritosRcd(Optional<Distritos> distritos) {
        this(distritos.get().getId(),
                distritos.get().getNome(),
                distritos.get().getCidades().getId(),
                distritos.get().getCidades().getNome());
    }

    public String getNome(){
                return nome.toUpperCase();
        }
}
